package Tramas;
public class PrimeraTramaAsociacion {
	public String Asociacion1(String RecipientAdd,String SenderAdd,String PKxA,String PKyA,String Witness_A ){
		CalculoMac MACTrama=new CalculoMac();
		String MAC=MACTrama.Mac();
		String MgnTrama = "4142000002030200"; /* Mac Header */
		MgnTrama = MgnTrama.concat(RecipientAdd).concat(SenderAdd).concat("80000000");
		String MK="0000000000000000";
		String TramaAsociacion=MgnTrama.concat(Witness_A).concat(PKxA).concat(PKyA).concat(MK);
		String CalCRC = "0x" +TramaAsociacion;
		String asociacion = CalCRC;
		CalculoCRC CRCTramaManagment=new CalculoCRC();
		String CRC=new String();
		CRC=CRCTramaManagment.Crc(asociacion);
		TramaAsociacion=TramaAsociacion.concat(CRC);
		return TramaAsociacion;
	}
}
