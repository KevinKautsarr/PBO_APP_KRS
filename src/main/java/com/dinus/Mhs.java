package com.dinus;

public class Mhs {
      private String nim;
      private String nama;
	  private String alamat;
      // constructor
      public Mhs(String nim, String nama,String alamat) {
         this.nim = nim;
         this.nama = nama;
		 this.alamat = alamat;
      }
	  public String getNim(){
		  return nim;
	  }
	  public String getNama(){
		  return nama;
	  }
	  public String getAlamat(){
		  return alamat;
	  }	  
   }