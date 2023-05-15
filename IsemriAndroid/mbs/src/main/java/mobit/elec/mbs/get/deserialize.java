package mobit.elec.mbs.get;

public interface deserialize {
    int deserialize(ICommand cmd, String row) throws Exception;
}
