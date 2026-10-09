package entity;

import database.CameraDAO;

public class EntityCamera {
	
	protected int identificativo;
	protected int numeroCamera;
	protected String stato;
	protected EntityAlbergo albergo;
	
	/**
	 * Costruttore di default
	 * */
	public EntityCamera() {
		super();
		
		this.albergo = new EntityAlbergo();
	}
	
	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @param identificativo Identificativo della camera
	 * */
	public EntityCamera(int identificativo) {
		super();
		
		this.identificativo = identificativo;
		
		CameraDAO camera = new CameraDAO(identificativo);
		
		this.numeroCamera = camera.getNumeroCamera();
		this.stato = camera.getStato();
		
		caricaAlbergo(camera);
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param camera Camera già costruita
	 * */
	public EntityCamera(CameraDAO camera) {
		this.identificativo = camera.getIdentificativo();
		this.numeroCamera = camera.getNumeroCamera();
		this.stato = camera.getStato();
		
		caricaAlbergo(camera);
	}
	
	/** 
	 * Funzione per caricare l'albergo della camera
	 * 
	 * @param camera Oggetto DAO già costruito
	 * */
	public void caricaAlbergo(CameraDAO camera) {
		EntityAlbergo albergo = new EntityAlbergo();
		
		albergo.setIdentificativo(camera.getAlbergo().getIdentificativo());
		albergo.setNome(camera.getAlbergo().getNome());
		albergo.setCitta(camera.getAlbergo().getCitta());
		albergo.setIndirizzo(camera.getAlbergo().getIndirizzo());
		albergo.setCAP(camera.getAlbergo().getCAP());
		albergo.setNumeroDiTelefono(camera.getAlbergo().getNumeroDiTelefono());
		
		this.albergo = albergo;
	}
	
	public int getIdentificativo() {
		return identificativo;
	}
	
	public void setIdentificativo(int identificativo) {
		this.identificativo = identificativo;
	}
	
	public int getNumeroCamera() {
		return numeroCamera;
	}
	
	public void setNumeroCamera(int numeroCamera) {
		this.numeroCamera = numeroCamera;
	}
	
	public String getStato() {
		return stato;
	}
	
	public void setStato(String stato) {
		this.stato = stato;
	}
	
	public EntityAlbergo getAlbergo() {
		return albergo;
	}
	
	public void setAlbergo(EntityAlbergo albergo) {
		this.albergo = albergo;
	}

	@Override
	public String toString() {
		return "\nNumero Camera: " + numeroCamera + 
	           "\nAlbergo: " + albergo.getNome();
	}
	
}