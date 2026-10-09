package DTO;

public class AutoreAttivitaDTO {
	private String nomeCompleto;
    private int numPoesie;
 
    public AutoreAttivitaDTO(String nomeCompleto, int numPoesie) {
        this.nomeCompleto = nomeCompleto;
        this.numPoesie = numPoesie;
    }
    public String getNomeCompleto() { return nomeCompleto; }
    public int getNumPoesie() { return numPoesie; }
}
