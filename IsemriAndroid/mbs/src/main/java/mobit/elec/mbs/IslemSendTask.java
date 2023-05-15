package mobit.elec.mbs;

import java.util.concurrent.Callable;

import com.mobit.IApplication;
import com.mobit.IIslem;

public class IslemSendTask implements Callable<Exception>  {
	
	protected IIslem islem;
	protected IApplication app;
	protected IMbsServer mbsServer;
	
	public IslemSendTask(IApplication app, IIslem islem) {
		this.app = app;
		this.islem = islem;
		mbsServer = (IMbsServer)app.getServer(0);
	}
	
	public Exception call() {

		Exception ex = null;
		return ex;
	
	}

}
