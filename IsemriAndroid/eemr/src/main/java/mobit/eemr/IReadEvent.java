package mobit.eemr;

public interface IReadEvent {

	int DataLine(String pAddress, String pDataLine);
	int QueryError(String pAddress, int tryIndex);
	boolean NextAddress(int[] ppPosition, String[] pAddress);
	int EnergyProfile(String pAct, String pEnd, String pCap, String pTime);
	int CurrentProfile(String pIL1, String pIL2, String pIL3, String pTime);
	int VoltageProfile(String pVL1, String pVL2, String pVL3, String pTime);
}
