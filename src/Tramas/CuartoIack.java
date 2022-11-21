package Tramas;
public class CuartoIack {
    public String IACK4(String TramaIACK_1){
	String seq1=TramaIACK_1.substring(0,4);
	String seq2= TramaIACK_1.substring(4,6).replace("00","03");
	String seq3= TramaIACK_1.substring(6,16);
	String TramaIACK_4= seq1.concat(seq2).concat(seq3);
	String CalCRC = "0x" +TramaIACK_4;
	String iack = CalCRC;
	CalculoCRC CRCIack=new CalculoCRC();
	String CRC=new String();
	CRC=CRCIack.Crc(iack);
	TramaIACK_4=TramaIACK_4.concat(CRC);	
	return TramaIACK_4;
    }
}