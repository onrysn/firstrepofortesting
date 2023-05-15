package mobit.elec.mbs.get;


public interface IDeserialize {

	public int deserialize(ICommand cmd, String row) throws Exception;

}
