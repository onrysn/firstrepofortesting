package mobit.elec.osos.viko;

import com.mobit.IApplication;
import com.mobit.IForm;
import mobit.elec.osos.viko.OperationService.ModemSearchType;
import mobit.elec.osos.viko.OperationService.Param;

public interface IOperationService {


	void ServiceLogin(String username, String password, final com.mobit.Callback clb) throws Exception;
	void OsosLogin(String token, final String username, String password, com.mobit.Callback clb) throws Exception;
	void SearchWirings(String token, String searchText, com.mobit.Callback clb) throws Exception;
	void SearchAmr(String token, String searchText, ModemSearchType searchType, com.mobit.Callback clb) throws Exception;
	void AddReadCommandtoAmr(String token, int ExternalCommandId, long AmrSerNo, long WiringSerNo, com.mobit.Callback clb) throws Exception;
	void GetAmrReadCommands(String token, long AmrSerNo, int LastCommandId, com.mobit.Callback clb) throws Exception;
	
	long getExternalCommandId();
	void OsosAktivasyon(String tesisat_no, Param param) throws Exception;
	void OsosAktivasyon(IApplication app, IForm form, final String tesisat_no, final Param param);
}
