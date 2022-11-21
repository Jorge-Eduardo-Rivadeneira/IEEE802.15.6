package Tramas;

public class TercerIack {
    public String IACK3(String TramaIACK_2){
	String seq1=TramaIACK_2.substring(0,4);
	String seq2= TramaIACK_2.substring(4,6).replace("01","02");
	String seq3= TramaIACK_2.substring(6,16);
	String TramaIACK_3= seq1.concat(seq2).concat(seq3);
	String CalCRC = "0x" +TramaIACK_3;
	String iack = CalCRC;
	CalculoCRC CRCIack=new CalculoCRC();
	String CRC=new String();
	CRC=CRCIack.Crc(iack);
	TramaIACK_3=TramaIACK_3.concat(CRC);
	return TramaIACK_3;
    }
}
