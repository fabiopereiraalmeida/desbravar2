package br.com.grupocaravela.configuracao;

public class Empresa {

	private static String nomeEpresa = "Empresa";
	private static String ipServidor = "192.168.0.2";
	private static String portaHttpServidor = "80";
	private static String portaMysqlServidor = "3306";
	
	private static String razaoSocial = "";
	private static String cnpj = "";
	private static String inscricaoEstadua = "";
	private static String endereco = "";
	private static String enderecoNumero = "";
	private static String bairro = "";
	private static String cidade = "";
	private static String cep = "";
	private static String telefonePrincipal = "";
	private static String telefoneSecundario = "";
	
	private static String frase = "";
	
	public static String getNomeEpresa() {
		return nomeEpresa;
	}
	public static String getIpServidor() {
		return ipServidor;
	}
	
	public static String getPortaHttpServidor() {
		return portaHttpServidor;
	}
	
	public static String getPortaMysqlServidor() {
		return portaMysqlServidor;
	}
	
	public static void setNomeEpresa(String nomeEpresa) {
		Empresa.nomeEpresa = nomeEpresa;
	}
	public static void setIpServidor(String ipServidor) {
		Empresa.ipServidor = ipServidor;
	}
	public static void setPortaHttpServidor(String portaHttpServidor) {
		Empresa.portaHttpServidor = portaHttpServidor;
	}
	public static void setPortaMysqlServidor(String portaMysqlServidor) {
		Empresa.portaMysqlServidor = portaMysqlServidor;
	}
	public static String getRazaoSocial() {
		return razaoSocial;
	}
	public static void setRazaoSocial(String razaoSocial) {
		Empresa.razaoSocial = razaoSocial;
	}
	public static String getCnpj() {
		return cnpj;
	}
	public static void setCnpj(String cnpj) {
		Empresa.cnpj = cnpj;
	}
	public static String getInscricaoEstadua() {
		return inscricaoEstadua;
	}
	public static void setInscricaoEstadua(String inscricaoEstadua) {
		Empresa.inscricaoEstadua = inscricaoEstadua;
	}
	public static String getEndereco() {
		return endereco;
	}
	public static void setEndereco(String endereco) {
		Empresa.endereco = endereco;
	}
	public static String getEnderecoNumero() {
		return enderecoNumero;
	}
	public static void setEnderecoNumero(String enderecoNumero) {
		Empresa.enderecoNumero = enderecoNumero;
	}
	public static String getBairro() {
		return bairro;
	}
	public static void setBairro(String bairro) {
		Empresa.bairro = bairro;
	}
	public static String getCidade() {
		return cidade;
	}
	public static void setCidade(String cidade) {
		Empresa.cidade = cidade;
	}
	public static String getCep() {
		return cep;
	}
	public static void setCep(String cep) {
		Empresa.cep = cep;
	}
	public static String getTelefonePrincipal() {
		return telefonePrincipal;
	}
	public static void setTelefonePrincipal(String telefonePrincipal) {
		Empresa.telefonePrincipal = telefonePrincipal;
	}
	public static String getTelefoneSecundario() {
		return telefoneSecundario;
	}
	public static void setTelefoneSecundario(String telefoneSecundario) {
		Empresa.telefoneSecundario = telefoneSecundario;
	}
	public static String getFrase() {
		return frase;
	}
	public static void setFrase(String frase) {
		Empresa.frase = frase;
	}
		
	
}
