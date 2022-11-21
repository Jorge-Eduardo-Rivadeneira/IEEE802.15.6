package Tramas;
public class PrimerIack {
    public String IACK(){
        String TramaIACK = "4007000003020200";
        String CalCRC = "0x" +TramaIACK;
        String iack = CalCRC;
	CalculoCRC CRCIack=new CalculoCRC();
	String CRC=new String();
        CRC=CRCIack.Crc(iack);
        TramaIACK=TramaIACK.concat(CRC);
	return TramaIACK;
    }
}
