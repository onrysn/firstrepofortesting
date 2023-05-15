package mobit.elec;

public interface INavigate {

	IIsemri prev(boolean unread) throws Exception;
	IIsemri next(boolean unread) throws Exception;
	
}
