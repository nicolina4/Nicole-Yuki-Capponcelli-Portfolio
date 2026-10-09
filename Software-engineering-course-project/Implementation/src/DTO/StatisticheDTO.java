package DTO;

import entity.PoesiaEntity;

public class StatisticheDTO {
	private int totaleCuori;
    private int totaleCommenti;
    private PoesiaEntity poesiaPiuApprezzata;
 
    public StatisticheDTO(int totaleCuori, int totaleCommenti, PoesiaEntity poesiaPiuApprezzata) {
        this.totaleCuori = totaleCuori;
        this.totaleCommenti = totaleCommenti;
        this.poesiaPiuApprezzata = poesiaPiuApprezzata;
    }
 
    public int getTotaleCuori() { return totaleCuori; }
    public int getTotaleCommenti() { return totaleCommenti; }
    public PoesiaEntity getPoesiaPiuApprezzata() { return poesiaPiuApprezzata; }
}
