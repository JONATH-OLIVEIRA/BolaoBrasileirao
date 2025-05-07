package br.com.apostas.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.apostas.enums.TipoUsuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private transient PasswordEncoder passwordEncoder;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Nome do usuario nao pode ser vazio")
	@Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
	private String nome;

	@NotBlank(message = "E-mail é obrigatório")
	@Email(message = "E-mail inválido")
	@Column(unique = true)
	private String email;

	@NotBlank(message = "Senha não pode estar vazia")
	@Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula e um número")
	private String senha;

	@NotBlank(message = "CPF é obrigatório")
	@Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
	@Column(unique = true)
	private String cpf;

	@Past(message = "Data de nascimento deve ser no passado")

	private LocalDate dtNascimento;

	@Enumerated(EnumType.STRING)
	private TipoUsuario tipo;

	private boolean ativo = true; // Usuário começa como ativo

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Aposta> apostas;

	/**
	 * Método para verificar se o usuário tem pelo menos 18 anos
	 */
	public boolean isMaiorDeIdade() {
		return Period.between(this.dtNascimento, LocalDate.now()).getYears() >= 18;
	}

	/**
	 * Método para "excluir" o usuário, tornando-o inativo
	 */
	public void desativarConta() {
		this.ativo = false;
	}

	/**
	 * Método para restaurar um usuário desativado (Somente admin pode fazer isso)
	 */
	public void restaurarConta() {
		this.ativo = true;
	}

	/**
	 * Metodo para criptografar a senha.
	 * 
	 */
	public void criptografarSenha() {
		this.senha = passwordEncoder.encode(this.senha);
	}

	/**
	 * Metodo para validar a senha.
	 * 
	 * @param senha
	 * @param encoder
	 * @return senha validada.
	 */
	public boolean validarSenha(String senha, PasswordEncoder encoder) {
		return encoder.matches(senha, this.senha);
	}

	public Usuario() {

	}

	public Usuario(Long id,
			@NotBlank(message = "Nome do usuario nao pode ser vazio") @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres") String nome,
			@NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,
			@NotBlank(message = "Senha não pode estar vazia") @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula e um número") String senha,
			@NotBlank(message = "CPF é obrigatório") @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos") String cpf,
			@Past(message = "Data de nascimento deve ser no passado") LocalDate dtNascimento, TipoUsuario tipo,
			boolean ativo, List<Aposta> apostas) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.cpf = cpf;
		this.dtNascimento = dtNascimento;
		this.tipo = tipo;
		this.ativo = ativo;
		this.apostas = apostas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(LocalDate dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public TipoUsuario getTipo() {
		return tipo;
	}

	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<Aposta> getApostas() {
		return apostas;
	}

	public void setApostas(List<Aposta> apostas) {
		this.apostas = apostas;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", cpf=" + cpf
				+ ", dtNascimento=" + dtNascimento + ", tipo=" + tipo + ", ativo=" + ativo + ", apostas=" + apostas
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}

}
