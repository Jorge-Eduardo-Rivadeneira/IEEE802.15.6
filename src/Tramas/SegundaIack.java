package Tramas;

public class SegundaIack {
    public String IACK2(String TramaIACK_1){
        String seq1=TramaIACK_1.substring(0,4);
	String seq2= TramaIACK_1.substring(4,6).replace("00","01");
	String seq3= TramaIACK_1.substring(6,16).replace("0003020200","0002030200");
	String TramaIACK_2= seq1.concat(seq2).concat(seq3);
	String CalCRC = "0x" +TramaIACK_2;
	String iack = CalCRC;
	CalculoCRC CRCIack=new CalculoCRC();
	String CRC=new String();
	CRC=CRCIack.Crc(iack);
	TramaIACK_2=TramaIACK_2.concat(CRC);
	return TramaIACK_2;
    }
}
