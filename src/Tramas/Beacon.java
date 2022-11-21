
package Tramas;

public class Beacon {
    public String Beacon1(){
        CalculoMac MACTrama=new CalculoMac(); //llamo a Calculo MAC para la direccion de la trama
        String MAC=MACTrama.Mac();
        String BeaconTrama = "01020000000102";// Mac Header
        BeaconTrama = BeaconTrama.concat(MAC);
        String BeaconTrama1="00000181000080FFF400FF"; //Frame Payload
        BeaconTrama=BeaconTrama.concat(BeaconTrama1);
        String CalCRC = "0x" +BeaconTrama;
        CalculoCRC CRCTramaBeacon=new CalculoCRC();//CAlculo del CRC
        String CRC=new String();
        CRC=CRCTramaBeacon.Crc(CalCRC);
        BeaconTrama=BeaconTrama.concat(CRC); //TramaFinal                
        return BeaconTrama;           
    }
}
