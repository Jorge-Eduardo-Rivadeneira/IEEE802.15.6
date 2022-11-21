package Tramas;

public class CuartaTramaAsociacion {
    public String Asociacion4(String TramaAsociacion_1,String Nonce_A,String MK_4A ){
        String seq1=TramaAsociacion_1.substring(0,4);
        String seq2= TramaAsociacion_1.substring(4,6).replace("00","03");
	String seq3= TramaAsociacion_1.substring(6,44);
	String seq4= TramaAsociacion_1.substring(44,45).replace("0", "3");
	String seq5= TramaAsociacion_1.substring(45,48);
	String PKxA=TramaAsociacion_1.substring(80,144);
	String PKyA=TramaAsociacion_1.substring(144,208);
	String seq6= Nonce_A;
	String seq7= PKxA;
	String seq8= PKyA;
	String seq9= MK_4A;
	String TramaAsociacion4= seq1.concat(seq2).concat(seq3).concat(seq4).concat(seq5).concat(seq6).concat(seq7).concat(seq8).concat(seq9);
	String CalCRC = "0x" +TramaAsociacion4;
	String asociacion = CalCRC;
	CalculoCRC CRCTramaManagment=new CalculoCRC();
	String CRC=new String();
        CRC=CRCTramaManagment.Crc(asociacion);
	TramaAsociacion4=TramaAsociacion4.concat(CRC);
	return TramaAsociacion4;
    }
}

