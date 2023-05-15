package mobit.elec.mbs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.GZIPInputStream;

import com.mobit.Callback;
import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IEleman;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.ILogin;
import com.mobit.LoginParam;
import com.mobit.MobitException;
import com.mobit.PageData;
import com.mobit.Server;
import mobit.elec.IAdurum;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.IIsemriSoru;
import mobit.elec.IIsemriSorular;
import mobit.elec.IKarne;
import mobit.elec.IMuhur;
import mobit.elec.IMuhurKod;
import mobit.elec.ISeriNo;
import mobit.elec.ITakilanSayac;
import mobit.elec.ISayacMarka;
import mobit.elec.ISdurum;
import mobit.elec.ITesisat;
import mobit.elec.IsemriFilterParam;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.put.put_endeks;
import mobit.elec.mbs.put.put_isemri;
import mobit.elec.mbs.put.put_isemri_birak;
import mobit.elec.mbs.put.put_isemri_sayac;
import mobit.elec.mbs.put.put_karne_birak;
import mobit.elec.mbs.put.put_opasswd;
import mobit.elec.mbs.put.put_optikdata;
import mobit.elec.mbs.put.put_version;
import mobit.elec.mbs.server.AutoResetEvent;
import mobit.elec.mbs.server.ByteBufferInputStream;
import mobit.elec.mbs.server.get_adurum;
import mobit.elec.mbs.server.get_eleman;
import mobit.elec.mbs.server.get_isemri_guncelle;
import mobit.elec.mbs.server.get_isemri_karne;
import mobit.elec.mbs.server.get_isemri_serbest;
import mobit.elec.mbs.server.get_isemri_soru;
import mobit.elec.mbs.server.get_isemri_sorular;
import mobit.elec.mbs.server.get_isemri_tesisat;
import mobit.elec.mbs.server.get_karne;
import mobit.elec.mbs.server.get_karne_muhur;
import mobit.elec.mbs.server.get_muhur;
import mobit.elec.mbs.server.get_muhur_durum;
import mobit.elec.mbs.server.get_muhur_kodlar;
import mobit.elec.mbs.server.get_okuyucu;
import mobit.elec.mbs.server.get_sayac;
import mobit.elec.mbs.server.get_sayac_markalar;
import mobit.elec.mbs.server.get_sdurum;
import mobit.elec.mbs.server.get_tesisat;
import mobit.elec.mbs.server.get_tesisat_muhur;
import mobit.elec.mbs.server.get_time;

public class MbsServer extends Server implements IMbsServer, ILogin {

	// blocked_socket bs = null;

	LoginParam loginParam = null;
	ExecutorService executor = null;


	@Override
	public boolean isTest(){

		return (tcpPort == 1111);
	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	@Override
	public void init(IApplication app) throws Exception {
		// TODO Auto-generated method stub
		super.init(app);
		if (executor != null){
			executor.shutdown();
			executor = null;
		}
		executor = Executors.newSingleThreadExecutor();
		blocked_socket(app.getHost(), app.getPort()); // "212.175.168.203", 1111

	}

	@Override
	public IEleman Login(LoginParam param) throws Exception {
		return login(param, 10000);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IAdurum> fetchAboneDurum(Integer ABONE_DURUM_KODU, Integer KOD_TIPI) throws Exception {
		// TODO Auto-generated method stub
		get_adurum ga = new get_adurum();
		execute(ga, 15000);
		List<IAdurum> list =  (List<IAdurum>) (List<?>) ga.getList();
		for(IAdurum durum : list) durum.setKOD_TIPI(KOD_TIPI);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ISdurum> fetchSayacDurum(Integer SAYAC_DURUM_KODU, Integer KOD_TIPI) throws Exception {
		// TODO Auto-generated method stub
		get_sdurum gs = new get_sdurum();
		execute(gs, 15000);
		List<ISdurum> list = (List<ISdurum>) (List<?>) gs.getList();
		for(ISdurum durum : list) durum.setKOD_TIPI(KOD_TIPI);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IIsemri> fetchIsemri(int zaman_kodu, boolean update) throws Exception {
		// TODO Auto-generated method stub
		get_isemri_guncelle gi = new get_isemri_guncelle(zaman_kodu);
		execute(gi, 120000);
		return (List<IIsemri>) (List<?>) gi.getList();
	}
	
	@Override
	public List<IIsemri> fetchIsemri(Integer ISEMRI_NO, IsemriFilterParam filter) throws Exception {
		// TODO Auto-generated method stub
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IIsemri> fetchKarneIsemri(int ZAMAN_KODU, Integer KARNE_NO) throws Exception {
		// TODO Auto-generated method stub
		get_isemri_karne gk = new get_isemri_karne(ZAMAN_KODU, KARNE_NO);
		execute(gk, 60000);
		return (List<IIsemri>)(List<?>) Collections.synchronizedList(gk.getList());
	}
	
	@Override
	public List<IIsemri> fetchKarneIsemri(Integer KARNE_NO, IsemriFilterParam filter) throws Exception {
		// TODO Auto-generated method stub
		return fetchKarneIsemri(0xffffffff, KARNE_NO);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<IIsemri> fetchTesisatIsemri(int ZAMAN_KODU, Integer TESISAT_NO) throws Exception {
		// TODO Auto-generated method stub
		get_isemri_tesisat gt = new get_isemri_tesisat(ZAMAN_KODU, TESISAT_NO);
		execute(gt, 15000);
		return (List<IIsemri>) (List<?>) Collections.synchronizedList(gt.getList());
	}
	
	@Override
	public List<IIsemri> fetchTesisatIsemri(Integer TESISAT_NO, IsemriFilterParam filter) throws Exception {
		// TODO Auto-generated method stub
		return fetchTesisatIsemri(0xffffffff, TESISAT_NO);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IKarne> fetchSerbestIsemri(int ZAMAN_KODU, IIslemTipi ISLEM_TIPI, int ALT_EMIR_TURU) throws Exception {
		// TODO Auto-generated method stub
		get_isemri_serbest gsi = new get_isemri_serbest(ZAMAN_KODU, ISLEM_TIPI, ALT_EMIR_TURU);
		execute(gsi, 45000);
		return (List<IKarne>) (List<?>)Collections.synchronizedList(gsi.getList());
	}
	@Override
	public List<IKarne> fetchSerbestIsemri(IIslemTipi ISLEM_TIPI, int ALT_EMIR_TURU, IsemriFilterParam filter) throws Exception {
		// TODO Auto-generated method stub
		return fetchSerbestIsemri(0xffffffff, ISLEM_TIPI, ALT_EMIR_TURU);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ITesisat> fetchKarneTesisat(Integer KARNE_NO, IsemriFilterParam filter) throws Exception {
		// TODO Auto-generated method stub
		get_karne gk = new get_karne(KARNE_NO, "");
		execute(gk, 30000);
		return (List<ITesisat>) (List<?>) Collections.synchronizedList(gk.getList());
	}
	
	@Override
	public void ismriKarneBirak(Integer KARNE_NO) throws Exception {
		// TODO Auto-generated method stub
		put_karne_birak kb = new put_karne_birak(KARNE_NO);
		execute(kb, 15000);
		
	}
	
	@Override
	public void isemriBirak(Integer TESISAT_NO, Integer SAHA_ISEMRI_NO) throws Exception {
		// TODO Auto-generated method stub
		put_isemri_birak ib = new put_isemri_birak(TESISAT_NO, SAHA_ISEMRI_NO);
		execute(ib, 15000);
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ITesisat> fetchTesisat(Integer TESISAT_NO) throws Exception {
		// TODO Auto-generated method stub
		get_tesisat gt = new get_tesisat(TESISAT_NO);
		execute(gt, 10000);
		return (List<ITesisat>) (List<?>) gt.getList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ITesisat> fetchTesisat2(Integer SAYAC_NO) throws Exception {
		// TODO Auto-generated method stub
		get_sayac gs = new get_sayac(SAYAC_NO);
		execute(gs, 20000);
		return (List<ITesisat>) (List<?>) gs.getList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IMuhurKod> fetchMuhurKod(Integer MUHUR_KOD) throws Exception {
		// TODO Auto-generated method stub
		get_muhur_kodlar gmk = new get_muhur_kodlar();
		execute(gmk, 15000);
		return (List<IMuhurKod>) (List<?>) gmk.getList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ITakilanSayac> fetchSayac(Integer SAYAC_NO) throws Exception {
		// TODO Auto-generated method stub
		get_sayac gs = new get_sayac(SAYAC_NO);
		execute(gs, 10000);
		return (List<ITakilanSayac>) (List<?>) gs.getList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ISayacMarka> fetchSayacMarka(String SAYAC_MARKA) throws Exception {
		// TODO Auto-generated method stub
		get_sayac_markalar gsm = new get_sayac_markalar();
		execute(gsm, 15000);
		return (List<ISayacMarka>) (List<?>) gsm.getList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IIsemriSoru> fetchIsemriSoru(Integer TESISAT_NO, Integer ISEMRI_NO) throws Exception {
		// TODO Auto-generated method stub
		get_isemri_soru gsm = new get_isemri_soru(TESISAT_NO, ISEMRI_NO);
		execute(gsm, 15000);
		return (List<IIsemriSoru>) (List<?>) gsm.getList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IIsemriSorular> fetchIsemriSorular() throws Exception {
		// TODO Auto-generated method stub
		get_isemri_sorular gsm = new get_isemri_sorular();
		execute(gsm, 15000);
		return (List<IIsemriSorular>) (List<?>) gsm.getList();
	}
	
	// -------------------------------------------------------------------------

	@Override
	public void sendIslem(final IForm form, IIslem islem, final Callback clb, int timeout) throws Exception {
		
		if(timeout == 0) timeout = 15000;
		
		if(islem instanceof ITakilanSayac){
			islem = new put_isemri_sayac(((ITakilanSayac)islem));
		}
		if(islem instanceof IIsemriIslem){
			IIsemriIslem iislem = (IIsemriIslem)islem;
			String data = iislem.getOPTIK_DATA();
			if(data != null && !data.isEmpty()){
				// Test ortamında her zaman 460 hatası dönüyor. Bu nedenle test ortamında gönderim kapatıldı
				// Test için değiştirilmesi gerekiyor
				//muhammed gökkaya
				if(/*true ||*/!app.isTest()) {
					put_optikdata pd = new put_optikdata(iislem.getTESISAT_NO(), data,iislem.getSAHA_ISEMRI_NO());
					execute((ICommand) pd, timeout);
				}
			}
		}
		
		execute((ICommand) islem, timeout);
		
	}

	@Override
	public void sendIslem(IIslem islem) throws Exception {

		sendIslem(null, islem, null, 0);
	}


	// -------------------------------------------------------------------------

	@Override
	public List<PageData> parse(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	// --------------------------------------------------------------------------
	// --------------------------------------------------------------------------
	// --------------------------------------------------------------------------

	private boolean running = true;
	private ByteBuffer inbuf = null;
	private ByteBuffer outbuf = null;

	private SocketChannel socket = null;
	private InetAddress serverAddr = null;
	private int tcpPort = 0;
	private Selector selector = null;

	@SuppressWarnings("unused")
	private String ServerInfo = "";
	@SuppressWarnings("unused")
	private SelectionKey connectKey = null;
	@SuppressWarnings("unused")
	private SelectionKey readKey = null;

	private final Lock _mutex = new ReentrantLock(true);
	private AutoResetEvent cmdEvent = new AutoResetEvent(false);
	private AutoResetEvent completedEvent = new AutoResetEvent(false);

	private StringBuilder sbuf = null;
	private ICommand m_cmd = null;

	private final String SERVER_INFO = "Endeksor Remote ";
	private final String BEGIN = "__BEGIN__";
	private final String END = "__END__";
	private final String QUIT = "QUIT";

	private final int sEND = END.length();

	private TimeZone zone = TimeZone.getTimeZone("Turkey");
	private DateFormat timeFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
	private DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");

	//private int ELEMAN_KODU = 0;
	//private int ELEMAN_SIFRE = 0;

	private boolean logining = false;
	private boolean logined = false;
	private boolean connected = false;
	private boolean completed = false;
	private int offset = 0;
	private int total = 0;
	private int mode = 0;

	private volatile Exception ex = null;

	private IEleman kullanici = null;

	public void blocked_socket(String host, int port) {

		inbuf = ByteBuffer.allocateDirect(1 * 1024 * 1024);
		outbuf = ByteBuffer.allocateDirect(0);
		sbuf = new StringBuilder(2 * 1024);
		timeFormatter.setTimeZone(zone);
		dateFormatter.setTimeZone(zone);

		try {

			serverAddr = InetAddress.getByName(host);
			tcpPort = port;
		} catch (Exception e) {
		}
		thread.start();

	}

	@Override
	public void close() {

		running = false;

		if (thread != null) {

			thread.interrupt();

			try {
				thread.join(Globals.isDeveloping()? 0 : 2000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
			thread = null;

			if (executor != null) {
				ExecutorService executor = this.executor;
				this.executor = null;
				executor.shutdown();
			}
		}
	}

	private Thread thread = new Thread(new Runnable() {

		public void run() {

			Throwable ex;
			try {

				loop();
				disconnect.run();

			} catch (Throwable e) {
				ex = e;
				app.getPlatform().Log(e);
				ex.printStackTrace();
			}


		}

	});

	private void dispatch() throws Exception {

		completed = false;
		offset = 0;
		total = 0;
		mode = 0;

		inbuf.clear();

		while (running && completed == false && selector.select() > 0) {

			Set<SelectionKey> keys = selector.selectedKeys();
			for (Iterator<?> i = keys.iterator(); i.hasNext();) {

				if (!running)
					break;
				SelectionKey key = (SelectionKey) i.next();
				i.remove();

				if (key.isReadable()) {
					recv();
				} else if (key.isWritable()) {
					send();
				} else if (key.isConnectable()) {
					readKey = socket.register(selector, SelectionKey.OP_READ);
					socket.finishConnect();

				}
			}
		}

		if(!running) return;

		if (completed) {
			switch (mode) {
			case 3:
				result_recv(array(inbuf, offset), offset);
				break;
			case 4:
				gzip_recv(inbuf, offset);
				break;
			case 1:
				connect_recv(array(inbuf, offset), offset);
				break;
			case 5:
				get_time(array(inbuf, offset), offset);
				break;
			default:
				throw new MobitException("Hatalı mod :" + mode);

			}
		}

	}

	private static byte[] array(ByteBuffer b, int size) {
		int position = b.position();
		byte[] arr = new byte[size];
		try {
			b.position(0);
			b.get(arr);
		} finally {
			b.position(position);
		}
		return arr;
	}

	private void loop() {

		while (running) {

			try {

				cmdEvent.waitOne();

			} catch (InterruptedException e) {
				continue;
			}

			if (!running)
				break;

			inbuf.clear();
			outbuf.clear();

			try {

				if (ex == null) {
					try {

						if (isConnected()) {

							int read = socket.read(inbuf);
							if (read > 0)
								inbuf.clear();
						} else {
							disconnect.run();
							connectInt();
						}

						if (m_cmd != null) {
							sbuf.setLength(0);
							m_cmd.toSerialize(sbuf);
							outbuf = ByteBuffer.wrap(sbuf.toString().getBytes());
							send();
						}

						dispatch();

					} catch (Exception e) {
						if (!(e instanceof ClosedChannelException)) {
							ex = e;
						}
					}
				}
			} finally {

				completedEvent.set();

			}

		}
	}

	private void send() throws IOException {
		socket.write(outbuf);
	}

	public IEleman login(LoginParam param, int timeout) throws Exception {

		/*
		 * if (mobit.Globals.developing) {execute return new eleman(1111,
		 * "TEST", new Date(), "", "000000", "", 1111); }
		 */

		if (logined)
			return kullanici;
		if (logining)
			return null;

		try {

			logining = true;
			
			if(param == null) param = loginParam;
			if (param == null) {
				if (!Globals.isDeveloping())
					throw new MobitException("Önce login olunması gerekiyor!");
			}
			
			get_time gt = new get_time();
			execute(gt, timeout);
			Date time = gt.getUTC();
			if (time != null)
				app.getPlatform().setTime(time);
			execute(new put_version(), timeout);

			if (param.mode == 1) {
				get_okuyucu go = new get_okuyucu(param.kullanici_kodu, param.kullanici_sifre);
				execute(go, timeout);
				if (go.getList().size() > 0) {
					kullanici = (IEleman) go.getList().get(0);
					kullanici.setELEMAN_SIFRE(param.kullanici_sifre);
				}
			} else {
				get_eleman ge = new get_eleman(param.kullanici_kodu, param.kullanici_sifre);
				execute(ge, timeout);
				if (ge.getList().size() > 0) {
					kullanici = (IEleman) ge.getList().get(0);
					kullanici.setELEMAN_SIFRE(param.kullanici_sifre);
				}
			}

			logined = true;

			this.loginParam = param;

		} finally {

			logining = false;
		}

		return kullanici;
	}

	@Override
	public void Logout() throws Exception {
		kullanici = null;
		loginParam = null;
		disconnect();
	}

	@SuppressWarnings("unused")
	public void execute(ICommand cmd, int timeout) throws Exception {

		/*
		if (Globals.isDeveloping())
			timeout = 5 * 60 * 1000;
		*/
		
		Exception t = null;
		
		block: try {

			app.getPlatform().checkInternetConnection();
			
			if (_mutex.tryLock(timeout, TimeUnit.MILLISECONDS) == false) {
				// break block;
				throw new MobitException(String.format("Zaman aşımı süresi %d ms doldu!", timeout));
			}
			
			
			int tryCount = 0;
			while (tryCount < 2) {
				try {

					login(loginParam, timeout);

					m_cmd = cmd;
					ex = null;
					cmdEvent.set();
					completedEvent.waitOne(timeout);
					t = null;

				} catch (Exception e) {

					ex = e;

				} finally {

					if (ex != null && m_cmd != null)
						m_cmd.getList().clear();
					m_cmd = null;
				}
				if (ex != null) {

					t = this.ex;
					this.ex = null;
					// Zaman aşımı oluşmuş ise yeniden bağlantı oluşturmak için
					// bağlantıyı kopar
					if (t instanceof TimeoutException) {
						// ErrnoException
						disconnect();
						tryCount++;
						continue;
					} 
					else if(t instanceof ConnectException){
						ConnectException e = (ConnectException)t;
						disconnect();
						String msg = e.getMessage();
						if(msg.contains("ENETUNREACH") || msg.contains("Network is unreachable")){
							// Cihaz yeni açıldığında hemen GPRS bağlantı sağlanmıyor
							// Biraz bekleyip tekrar denenmesi
							Thread.sleep(5000);
						}
						tryCount++;
						continue;
					}
					else if (t instanceof SocketException || t instanceof IOException) {
						//if(Globals.platform != null) Globals.platform.Log(ex);
						//IOException - Software caused connection abort
						//SocketException - ETIMEDOUT
						disconnect();
						tryCount++;
						continue;
					}
					else if(t instanceof InterruptedException){
						// Eğer kullanıcı işlemi iptal etmiş ise bağlantıyı kopar
						disconnect();
					}
					
				}
				break;
			}
		} finally {

			_mutex.unlock();
		}
		if(t != null) 
			throw t;
	}

	public boolean isConnected() {

		return (socket != null && socket.isConnected());
	}

	//-------------------------------------------------------------------------

	private void disconnect() {

		logined = false;
		connected = false;

		if(cmdEvent != null) cmdEvent.reset();
		if (thread != null)  thread.interrupt();

		if(executor != null) {
			try {
				Future<?> future = executor.submit(disconnect);
				future.get(Globals.isDeveloping() ? 3600000 : 2000, TimeUnit.MILLISECONDS);

			} catch (Exception e) {
			}
		}
	}

	Runnable disconnect = new Runnable() {

		public void run() {

			synchronized (this) {

				logined = false;
				connected = false;

				if (socket != null && socket.isOpen()) {
					try {
						// Mbs'ye bağlantının kapatılacağının bildirilmesi
						ByteBuffer bb = ByteBuffer.wrap(String.format("%s\n", QUIT).getBytes());
						socket.write(bb);
						Thread.sleep(10);

					} catch (Exception e) {
					}
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (selector != null && selector.isOpen()) {
					try {
						selector.wakeup();
						selector.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	};

	//-------------------------------------------------------------------------

	public void connect() throws Exception
	{
		if (isConnected())
			return;

		disconnect();
		connectInt();
	}

	private void connectInt() throws Exception
	{

		synchronized (this) {
			selector = Selector.open();
			socket = SocketChannel.open();
			socket.configureBlocking(false);
			// API 24'te destekleniyor
			// socket.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			// socket.setOption(StandardSocketOptions.TCP_NODELAY, true);
			connectKey = socket.register(selector, SelectionKey.OP_CONNECT);
			// key = socket.register(selector,
			// SelectionKey.OP_CONNECT|SelectionKey.OP_READ/*socket.validOps()*/);
			socket.connect(new InetSocketAddress(serverAddr, tcpPort));
		}

		dispatch();

	}

	private int get_lenght(byte[] buf) {

		int lenght = 0;
		String s = "";
		for (int i = 0; i < buf.length && i < 30; i++) {
			if (inbuf.get(i) == '\n') {
				int idx1, idx2;
				String header = new String(buf, 0, i);
				if (header.startsWith(BEGIN, 0) == false)
					break;
				idx1 = header.indexOf("(");
				idx2 = header.indexOf(")");
				if (idx1 < 0 || idx2 < 0)
					break;
				s = header.substring(idx1 + 1, idx2);
			}
		}
		lenght = Integer.parseInt(s);
		return lenght;
	}

	private void recv() throws UnsupportedEncodingException, IOException, MbsException, InstantiationException,
			IllegalAccessException, ParseException, InterruptedException {

		int read;
		String header;

		while (running && completed == false && (read = socket.read(inbuf)) > 0) {

			offset += read;
			if (total == 0) {

				byte[] arr = array(inbuf, read > 120 ? 120 : read);
				header = new String(arr, "UTF-8");

				if (header.contains(BEGIN)) {
					int idx = header.indexOf('\n');
					total = idx + 1/* \n */ + get_lenght(arr) + sEND + 1/* \n */;
					mode = 4;
					if (offset == total)
						completed = true;
				} else if (connected == true && m_cmd != null) {
					if (m_cmd instanceof get_time) {
						mode = 5;
					} else {
						mode = 3;
					}
					completed = true;
				} else if (header.contains(SERVER_INFO)) {
					mode = 1;
					completed = true;
				} else {
					mode = 0;
					completed = true;
				}
			} else {
//HÜSEYİN EMRE ÇEVİK KARNE SORUNU
				if (offset == total|| offset== (total+57))
				completed = true;
			}
		}

	}

	private void get_time(byte[] buf, int length) throws Exception {
		String b = new String(buf, 0, length);
		m_cmd.deserialize(b);

	}

	private void result_recv(byte[] inbuf, int offset) throws InstantiationException, IllegalAccessException,
			UnsupportedEncodingException, MobitException, ParseException {

		m_cmd.getResult(new String(inbuf, 0, offset));

	}

	private void connect_recv(byte[] buf, int lenght) throws UnsupportedEncodingException, InterruptedException {
		int i = 0;
		for (i = 0; i < lenght; i++)
			if (buf[i] == '\n')
				break;
		ServerInfo = new String(buf, 0, i, "UTF-8");
		connected = true;

	}

	private void gzip_recv(ByteBuffer buf, int length) throws Exception {

		int i;
		byte ch;
		boolean found = false;

		buf.limit(length - (sEND + 1));
		buf.position(0);

		for (i = 0; i < length && i < 30; i++) {
			ch = buf.get();
			if (ch == '\n') {
				found = true;
				break;
			}
		}

		if (found == false)
			throw new MobitException("Satır sonu karakteri bulunamadı!");

		InputStream is = null;
		GZIPInputStream gzis = null;
		InputStreamReader reader = null;
		BufferedReader br = null;
		String readed = "";
		try {

			// is = new ByteArrayInputStream(buf, i+1, length-(i+1+sEND+1)); //
			// buf.length-(i+1+sEND+1)
			is = new ByteBufferInputStream(buf);
			gzis = new GZIPInputStream(is);
			reader = new InputStreamReader(gzis, "UTF-8");

			br = new BufferedReader(reader);

			if (m_cmd instanceof put_isemri || m_cmd instanceof put_endeks) {

				sbuf.setLength(0);
				while (running && ((readed = br.readLine()) != null)) {

					if (!readed.contains("form_tipi") && !readed.contains("maliye_muhur")){
						readed = readed.trim();
					}
					if (readed.length() > 0) {
					//	System.out.println("Hüseyin emre çevik readed = "+readed);
						sbuf.append(readed);
						sbuf.append('\n');
					}
				}
				m_cmd.deserialize(sbuf.toString());

			} else {

				while (running && ((readed = br.readLine()) != null)) {

					readed = readed.trim();
					if (readed.length() > 0)
						m_cmd.deserialize(readed);

				//	System.out.println("Hüseyin emre çevik readed = "+readed);
				}
			}

			readed = null;

		} catch (ClassCastException e) {
			throw e;
		} catch (InstantiationException e) {
			throw e;
		} catch (Exception e) {

			throw new MobitException(String.format("Kayıt hatası\n%s", readed), "Hata", e);

		} finally {

			if (br != null)
				br.close();
			if (reader != null)
				reader.close();
			if (gzis != null)
				gzis.close();
			if (is != null)
				is.close();
		}

	}

	/*
	 * public Date getTime() throws ParseException { DateFormat timeFormatter1 =
	 * new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss"); DateFormat timeFormatter2 =
	 * new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
	 * timeFormatter1.setTimeZone(zone);
	 * timeFormatter2.setTimeZone(TimeZone.getTimeZone("UTC")); String s =
	 * timeFormatter1.format(getUTCTime()); Date d = timeFormatter2.parse(s);
	 * return d; }
	 */

	@Override
	public Date getUTCTime() {

		return app.getPlatform().getTime();

	}

	public String getTIMESTAMP() {
		return timeFormatter.format(getUTCTime());
	}

	public String getDATESTAMP() {

		return dateFormatter.format(getUTCTime());
	}
	
	@Override
	public void ChangePassword(int kullaniciKodu, int sifre, int yeniSifre)throws Exception
	{
		put_opasswd pwd = new put_opasswd(kullaniciKodu, sifre, yeniSifre);
		execute(pwd, 10000);
	}
	@Override
	public List<IMuhur> fetchKarneMuhur(int KARNE_NO) throws Exception
	{
		get_karne_muhur gkm = new get_karne_muhur(KARNE_NO);
		execute(gkm, 30000);
		List<IMuhur> list =  (List<IMuhur>) (List<?>) gkm.getList();
		return list;
	}
	@Override
	public List<IMuhur> fetchTesisatMuhur(int TESISAT_NO) throws Exception
	{
		get_tesisat_muhur gtm = new get_tesisat_muhur(TESISAT_NO);
		execute(gtm, 15000);
		List<IMuhur> list =  (List<IMuhur>) (List<?>) gtm.getList();
		return list;
	}
	@Override
	public List<IMuhur> fetchMuhur(ISeriNo muhur) throws Exception
	{
		get_muhur gm = new get_muhur(muhur);
		execute(gm, 15000);
		List<IMuhur> list =  (List<IMuhur>) (List<?>) gm.getList();
		return list;
	}
	@Override
	public List<IMuhur> fetchMuhurDurum(ISeriNo muhur) throws Exception
	{
		get_muhur_durum gmd = new get_muhur_durum(muhur);
		execute(gmd, 15000);
		List<IMuhur> list =  (List<IMuhur>) (List<?>) gmd.getList();
		return list;
	}

}
