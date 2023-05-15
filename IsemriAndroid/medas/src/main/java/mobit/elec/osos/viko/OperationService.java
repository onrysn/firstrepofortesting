package mobit.elec.osos.viko;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import com.mobit.IApplication;
import com.mobit.IEleman;
import com.mobit.IForm;
import com.mobit.ILogin;
import com.mobit.IServer;
import com.mobit.LoginParam;
import com.mobit.MobitException;
import mobit.eemr.IReadResult;
import mobit.eemr.ReadResult;
import mobit.elec.Globals;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.elec.mbs.medas.MedasApplication;
import mobit.elec.osos.viko.OperationService.Param;
import mobit.http.Result;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class OperationService implements IOperationService, IServer, ILogin {

	public static class VikoOsosException extends MobitException {

		Result result;
		private static final long serialVersionUID = -6359575296363557498L;
		private static final String s_caption = "Viko OSOS Hata";

		public VikoOsosException(String message) {
			super(message, s_caption);
		}

	}

	private IMedasApplication app;
	private volatile long ExternalCommandId = 0;
	private static final String URL = "http://SOS/Externervices/OperationService.svc/json/";
	private Gson gson = null;
	private Retrofit retrofit = null;
	private IService apiService = null;
	protected String serviceUsername = "Osnal";
	protected String servicePassword = "Ha33";

	private String mToken = null;

	private String mUsername = null;
	protected String ososUsername = "ramazala";
	protected String ososPassword = "vhV9C";

	@Override
	public boolean isTest()
	{
		return false;
	}

	public synchronized long getExternalCommandId() {
			
		/*
		Date date = new Date();
		ExternalCommandId = date.getTime();
		//Random rand = new Random();
		//ExternalCommandId = rand.nextInt(Integer.MAX_VALUE);
		//ExternalCommandId %= 1*24*60*60*1000;
		ExternalCommandId = 2002;
		*/
		return ++ExternalCommandId;
	}

	@Override
	public void init(IApplication app) throws Exception {
		// TODO Auto-generated method stub
		this.app = (IMedasApplication) app;

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connect() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Date getUTCTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEleman Login(LoginParam param) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Logout() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void ChangePassword(int kullaniciKodu, int sifre, int yeniSifre) throws Exception {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------
	
	public static class Param {

		public com.mobit.Callback clb;
		public String serviceUsername;
		public String servicePassword;

		public String token;

		public String ososUsername;
		public String ososPassword;

		public String searchText;
		public long ExternalCommandId;

		public long WiringSerno;
		public long SerNo;
		
		public int trycount = 0;
		
		public long beginTime = 0;
		
		public boolean canceled = false;
		
		private Object result = null; 
		

		public Param() {
			ExternalCommandId = 1;
		}
		public Param(long ExternalCommandId) {
			this.ExternalCommandId = ExternalCommandId;
		}
		
		public void setResult(Object result)
		{
			this.result = result;
			synchronized(this){ notify();}
		}
		public Object getResult()
		{
			return result;
		}
		
	}
	
	// -------------------------------------------------------------------------

	public static enum ExternalResultStatus {
		Error(0), Success(1), Warning(2);

		private int value;

		private ExternalResultStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static ExternalResultStatus fromInteger(int value) {
			switch (value) {
			case 0:
				return Error;
			case 1:
				return Success;
			case 2:
				return Warning;
			}

			return null;
		}
	};

	public static enum ModemSearchType {
		ModemNo("0"), SimId("1"), ModemIP("2"), ModemFromSayacNo("3"), ModemFromTesisatNo("4");

		private String value;

		private ModemSearchType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static ModemSearchType fromInteger(String value) {
			switch (value) {
			case "0":
				return ModemNo;
			case "1":
				return SimId;
			case "2":
				return ModemIP;
			case "3":
				return ModemFromSayacNo;
			case "4":
				return ModemFromTesisatNo;
			}

			return null;
		}

	}
	
	public static enum CommandStatus {
		SirayaAlindi(1), Isleniyor(2), Hata(3), BASARILI(4), IptalEdildi(5), ModemHatasi(6);

		private int value;

		private CommandStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static CommandStatus fromInteger(int value) {
			switch (value) {
			case 1:
				return SirayaAlindi;
			case 2:
				return Isleniyor;
			case 3:
				return Hata;
			case 4:
				return BASARILI;
			case 5:
				return IptalEdildi;
			case 6:
				return ModemHatasi;
			}
			return null;
		}

	};
	
	public static enum AnswerStatus {
		SIRAYA_ALINDI(1),
		ISLENIYOR(2),
		BASARISIZ(3),
		BASARILI(4);

		private int value;

		private AnswerStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static AnswerStatus fromInteger(int value) {
			switch (value) {
				case 1:
					return SIRAYA_ALINDI;
				case 2:
					return ISLENIYOR;
				case 3:
					return BASARISIZ;
				case 4:
					return BASARILI;
			}
			return null;
		}
	};
	

	public static enum MessageType {
		
		OkumaIstegi("03"), ObisIleOkumaIstegi("17"), Readout("02"), Readout2("95"), KonfigurasyonGuncelleme("42"), Reset("47");

		private String value;

		private MessageType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static MessageType fromString(String value) {
			switch (value) {
			case "03":
				return OkumaIstegi;
			case "17":
				return ObisIleOkumaIstegi;
			case "02":
				return Readout;
			case "95":
				return Readout2;
			case "42":
				return KonfigurasyonGuncelleme;
			case "47":
				return Reset;
			}
			return null;
		}
	}

	private static class Resultd<T> {
		@SerializedName("d")
		public ExternalResult<T> d;
	}

	private static class ExternalResult<T> {

		@SerializedName("__type")
		public String __type;
		@SerializedName("InfoDetail")
		public String InfoDetail;
		@SerializedName("Result")
		public T Result;
		@SerializedName("ResultStatus")
		private int ResultStatus;

		public ExternalResultStatus getResultStatus() {
			return ExternalResultStatus.fromInteger(ResultStatus);
		}

		public VikoOsosException checkStatus() {
			return (getResultStatus() == ExternalResultStatus.Error) ? new VikoOsosException(InfoDetail) : null;
		}

	}

	public static class WiringNoteDTO {

	}

	public static class WiringDefinitionDTO {
		
		@SerializedName("Adres")
		public String Adres;
		@SerializedName("ArmaturGucu")
		public Double ArmaturGucu;
		@SerializedName("ArmaturSayisi")
		public Integer ArmaturSayisi;
		@SerializedName("DirekSayisi")
		public Integer DirekSayisi;
		@SerializedName("Email")
		public String Email;
		@SerializedName("EndeksTarihi")
		public String EndeksTarihi;
		@SerializedName("FlowMultiplier")
		public Double FlowMultiplier;
		@SerializedName("KuruluGuc")
		public Double KuruluGuc;
		@SerializedName("MeterSerialNo")
		public String MeterSerialNo;
		@SerializedName("Notlar")
		public List<WiringNoteDTO> Notlar;
		@SerializedName("PanoNo")
		public String PanoNo;
		@SerializedName("PanoTipi")
		public Integer PanoTipi;
		@SerializedName("PhysicalGroupId")
		public Long PhysicalGroupId;
		@SerializedName("PhysicalGroupName")
		public String PhysicalGroupName;
		@SerializedName("SayacId")
		public UUID SayacId;
		@SerializedName("SeytDegeri")
		public Double SeytDegeri;
		@SerializedName("SonOkuma")
		public String SonOkuma;
		@SerializedName("SubscriberName")
		public String SubscriberName;
		@SerializedName("SubscriberSectorInfo")
		public String SubscriberSectorInfo;
		@SerializedName("T1Endeks")
		public Double T1Endeks;
		@SerializedName("T2Endeks")
		public Double T2Endeks;
		@SerializedName("T3Endeks")
		public Double T3Endeks;
		@SerializedName("Telefon")
		public String Telefon;
		@SerializedName("ToplamEndeks")
		public Double ToplamEndeks;
		@SerializedName("TrafoMarkasi")
		public String TrafoMarkasi;
		@SerializedName("TrafoTipi")
		public String TrafoTipi;
		@SerializedName("Trafoil")
		public String Trafoil;
		@SerializedName("Uevcb")
		public String Uevcb;
		@SerializedName("VirtualGroupGuids")
		public List<UUID> VirtualGroupGuids;
		@SerializedName("VirtualGroupNames")
		public String VirtualGroupNames;
		@SerializedName("VoltageMultiplier")
		public Double VoltageMultiplier;
		@SerializedName("WiringId")
		public UUID WiringId;
		@SerializedName("WiringNo")
		public String WiringNo;
		@SerializedName("WiringNote")
		public String WiringNote;
		@SerializedName("WiringSerno")
		public Long WiringSerno;
		@SerializedName("WiringType")
		public Integer WiringType;
	}

	public static class AmrDefinitionDTO {
		
		@SerializedName("AmrId")
		public String AmrId;
		@SerializedName("AmrRemoteId")
		public String AmrRemoteId;
		@SerializedName("ErisimDurumu")
		public String ErisimDurumu;
		@SerializedName("PhoneNumber")
		public String PhoneNumber;
		@SerializedName("SayacErisimDurumu")
		public Integer SayacErisimDurumu;
		@SerializedName("SerNo")
		public Long SerNo;
		@SerializedName("SignalLength")
		public Integer SignalLength;
		@SerializedName("SonErisimTarihi")
		public String SonErisimTarihi;
		@SerializedName("WiringSernos")
		public List<Long> WiringSernos;

	}
	
	public static class CommandDTO {
		
		@SerializedName("AmrSerNo")
		public Long AmrSerNo;
		@SerializedName("AnswerData")
		public String AnswerData;
		@SerializedName("AnswerSerno")
		public Long AnswerSerno;
		@SerializedName("AnswerStatus")
		public Integer AnswerStatus;
		@SerializedName("CommandSendType")
		public Integer CommandSendType;
		@SerializedName("CommandStatus")
		public Integer CommandStatus;
		@SerializedName("Message")
		public String Message;
		@SerializedName("MessageType")
		public String MessageType;
		@SerializedName("OperationDate")
		public String OperationDate;
		@SerializedName("RetryCount")
		public Integer RetryCount;
		@SerializedName("SerNo")
		public Long SerNo;
		@SerializedName("WiringNo")
		public String WiringNo;
		
		
		public MessageType getMessageType()
		{
			return OperationService.MessageType.fromString(MessageType);
		}
		public Date getOperationDate()
		{
			if(OperationDate == null || OperationDate.isEmpty())
				return null;
			
			
			try {
				int begin = OperationDate.indexOf('(');
				if(begin < 0) return null;
				begin++;
				int end = OperationDate.indexOf('+');
				if(end < 0) OperationDate.indexOf('-');
				String s = OperationDate.substring(begin, end);
				Date date = new Date(Long.parseLong(s));
				begin = end;
				end = OperationDate.indexOf(')');
				s = OperationDate.substring(begin, end);
				TimeZone zone = TimeZone.getTimeZone(s);

				Calendar theCalendar = Calendar.getInstance(zone);
				theCalendar.setTime(date);
				return theCalendar.getTime();
			}
			catch(Exception e){
			
			}
			return null;
		}

		public CommandStatus getCommandStatus()
		{
			return OperationService.CommandStatus.fromInteger(CommandStatus);
		}

		public AnswerStatus getAnswerStatus()
		{
			if(AnswerStatus == null) return null;
			return OperationService.AnswerStatus.fromInteger(AnswerStatus);
		}

		
	}
	
	public static class CommandListDTO {
		@SerializedName("Commands")
		public List<CommandDTO> Commands = new ArrayList<CommandDTO>();
	}

	
	private interface IService {

		@GET("ServiceLogin")
		Call<Resultd<String>> ServiceLogin(@Query("userName") String userName, @Query("password") String password);

		@GET("OsosLogin")
		Call<Resultd<String>> OsosLogin(@Query("token") String token, @Query("username") String username,
				@Query("password") String password);

		@GET("SearchWirings")
		Call<Resultd<WiringDefinitionDTO>> SearchWirings(@Query("token") String token,
				@Query("searchText") String searchText);

		@GET("SearchAmr")
		Call<Resultd<AmrDefinitionDTO>> SearchAmr(@Query("token") String token, @Query("searchText") String searchText,
				@Query("searchType") String searchType);

		@GET("AddReadCommandtoAmr")
		Call<Resultd<String>> AddReadCommandtoAmr(@Query("token") String token,
				@Query("ExternalCommandId") long ExternalCommandId, @Query("AmrSerNo") long AmrSerNo,
				@Query("WiringSerNo") long WiringSerNo);

		@GET("GetAmrReadCommands")
		Call<Resultd<CommandListDTO>> GetAmrReadCommands(@Query("token") String token, @Query("AmrSerNo") long AmrSerNo,
				@Query("LastCommandId") long LastCommandId);

	};

	// -------------------------------------------------------------------------

	private Retrofit getClient(String baseURL) {

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
		clientBuilder.connectTimeout(15, TimeUnit.SECONDS);
		clientBuilder.writeTimeout(15, TimeUnit.SECONDS);
		clientBuilder.readTimeout(60, TimeUnit.SECONDS);
		clientBuilder.retryOnConnectionFailure(false);
		
		if (Globals.isDeveloping()) {
			HttpLoggingInterceptor.Logger networkLayerLogger = new HttpLoggingInterceptor.Logger() {
				String s;

				@Override
				public void log(String message) {
					return;
				}
			};

			HttpLoggingInterceptor.Logger appLayerLogger = new HttpLoggingInterceptor.Logger() {
				String s;

				@Override
				public void log(String message) {
					s = message;
				}
			};
			HttpLoggingInterceptor networkLogging = new HttpLoggingInterceptor(networkLayerLogger);
			HttpLoggingInterceptor appLogging = new HttpLoggingInterceptor(appLayerLogger);

			networkLogging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
			appLogging.setLevel(HttpLoggingInterceptor.Level.BODY);

			clientBuilder.addNetworkInterceptor(networkLogging);
			clientBuilder.addInterceptor(appLogging);
		}
		
		OkHttpClient client = clientBuilder.build();		

		GsonBuilder builder = new GsonBuilder();
		//builder.setDateFormat(DateFormat.LONG);
		builder.registerTypeAdapter(Resultd.class, new Deserializer<Resultd<WiringDefinitionDTO>>());
		builder.registerTypeAdapter(Resultd.class, new Deserializer<Resultd<AmrDefinitionDTO>>());
		builder.registerTypeAdapter(Resultd.class, new Deserializer<Resultd<CommandListDTO>>());
		builder.registerTypeAdapter(Resultd.class, new Deserializer<Resultd<CommandDTO>>());
		builder.registerTypeAdapter(Resultd.class, new Deserializer<Resultd<Date>>());
		
		gson = builder.create();
		
		retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create(gson))
				.client(client).build();
		
		return retrofit;
	}

	private void initService() {
		if (retrofit == null)
			retrofit = getClient(URL);
		if (apiService == null)
			apiService = retrofit.create(IService.class);
	}
	
	private class Deserializer<T> implements JsonDeserializer<T> {
		@Override
		public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
			JsonElement result = je.getAsJsonObject();
			
			T t = new Gson().fromJson(result, type);
			return t;

		}
	}
	private static class DateDeserializer implements JsonDeserializer<Date> {
		@Override
		public Date deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
			JsonElement result = je.getAsJsonObject();
			
			return new Gson().fromJson(result, type);

		}
	}
	private static class CostumTypeAdapter<T> extends TypeAdapter<T> {
	   
	    @Override
	    public void write(JsonWriter out, T value) throws IOException {
	        
	    }

	    @Override
	    public T read(JsonReader reader) throws IOException {
	        if (reader.peek() == JsonToken.NULL) {
	            reader.nextNull();
	            return null;
	        }

	        String dateAsString = reader.nextString();
	        return null;
	    }
	    
	}

	// -------------------------------------------------------------------------

	public void ServiceLogin(String username, String password, final com.mobit.Callback clb) throws Exception {

		mToken = null;

		initService();

		Call<Resultd<String>> call = apiService.ServiceLogin(username, password);

		call.enqueue(new Callback<Resultd<String>>() {

			@Override
			public void onFailure(Call<Resultd<String>> arg0, Throwable arg1) {
				// TODO Auto-generated method stub
				clb.run(arg1);
			}

			@Override
			public void onResponse(Call<Resultd<String>> arg0, Response<Resultd<String>> arg1) {
				// TODO Auto-generated method stub

				Resultd<String> rd = arg1.body();
				ExternalResult<String> er = rd.d;
				VikoOsosException ex = er.checkStatus();
				if (ex != null) {
					clb.run(ex);
					return;
				}
				clb.run(er.Result);
				return;

			}

		});
	}

	// -------------------------------------------------------------------------

	public void OsosLogin(String token, final String username, String password, final com.mobit.Callback clb)
			throws Exception {

		mUsername = null;

		initService();

		Call<Resultd<String>> call = apiService.OsosLogin(token, username, password);
		call.enqueue(new Callback<Resultd<String>>() {

			@Override
			public void onFailure(Call<Resultd<String>> arg0, Throwable arg1) {
				// TODO Auto-generated method stub
				clb.run(arg1);
			}

			@Override
			public void onResponse(Call<Resultd<String>> arg0, Response<Resultd<String>> arg1) {
				// TODO Auto-generated method stub

				Resultd<String> rd = arg1.body();
				ExternalResult<?> er = rd.d;
				VikoOsosException ex = er.checkStatus();
				if (ex != null) {
					clb.run(ex);
					return;
				}
				mUsername = username;
				clb.run(er.ResultStatus);
				return;
			}

		});
	}

	// -------------------------------------------------------------------------

	public void SearchWirings(String token, String searchText, final com.mobit.Callback clb) throws Exception {

		initService();

		Call<Resultd<WiringDefinitionDTO>> call = apiService.SearchWirings(token, searchText);
		call.enqueue(new Callback<Resultd<WiringDefinitionDTO>>() {

			@Override
			public void onFailure(Call<Resultd<WiringDefinitionDTO>> arg0, Throwable arg1) {
				// TODO Auto-generated method stub
				clb.run(arg1);
			}

			@Override
			public void onResponse(Call<Resultd<WiringDefinitionDTO>> arg0,
					Response<Resultd<WiringDefinitionDTO>> arg1) {
				// TODO Auto-generated method stub

				Resultd<WiringDefinitionDTO> rd = arg1.body();
				ExternalResult<WiringDefinitionDTO> er = rd.d;
				VikoOsosException ex = er.checkStatus();
				if (ex != null) {
					clb.run(ex);
					return;
				}
				WiringDefinitionDTO wd = (WiringDefinitionDTO) er.Result;
				clb.run(wd);
				return;

			}

		});
	}

	// -------------------------------------------------------------------------

	public void SearchAmr(String token, String searchText, ModemSearchType searchType, final com.mobit.Callback clb)
			throws Exception {

		initService();

		Call<Resultd<AmrDefinitionDTO>> call = apiService.SearchAmr(token, searchText, searchType.getValue());

		call.enqueue(new Callback<Resultd<AmrDefinitionDTO>>() {

			@Override
			public void onFailure(Call<Resultd<AmrDefinitionDTO>> arg0, Throwable arg1) {
				// TODO Auto-generated method stub
				clb.run(arg1);
			}

			@Override
			public void onResponse(Call<Resultd<AmrDefinitionDTO>> arg0, Response<Resultd<AmrDefinitionDTO>> arg1) {
				// TODO Auto-generated method stub

				Resultd<AmrDefinitionDTO> rd = arg1.body();
				ExternalResult<AmrDefinitionDTO> er = rd.d;
				VikoOsosException ex = er.checkStatus();
				if (ex != null) {
					clb.run(ex);
					return;
				}
				AmrDefinitionDTO amr = (AmrDefinitionDTO) er.Result;
				clb.run(amr);
				return;

			}

		});
	}

	// -------------------------------------------------------------------------

	public void AddReadCommandtoAmr(String token, int ExternalCommandId, long AmrSerNo, long WiringSerNo,
			final com.mobit.Callback clb) throws Exception {

		initService();

		Call<Resultd<String>> call = apiService.AddReadCommandtoAmr(token, ExternalCommandId, AmrSerNo, WiringSerNo);

		call.enqueue(new Callback<Resultd<String>>() {

			@Override
			public void onFailure(Call<Resultd<String>> arg0, Throwable arg1) {
				// TODO Auto-generated method stub
				clb.run(arg1);
			}

			@Override
			public void onResponse(Call<Resultd<String>> arg0, Response<Resultd<String>> arg1) {
				// TODO Auto-generated method stub

				Resultd<String> rd = arg1.body();
				ExternalResult<String> er = rd.d;
				VikoOsosException ex = er.checkStatus();
				if (ex != null) {
					clb.run(ex);
					return;
				}
				clb.run(er.Result);
				return;

			}

		});
	}

	// -------------------------------------------------------------------------

	public void GetAmrReadCommands(String token, long AmrSerNo, int LastCommandId, final com.mobit.Callback clb)
			throws Exception {

		initService();

		Call<Resultd<CommandListDTO>> call = apiService.GetAmrReadCommands(token, AmrSerNo, LastCommandId);
		call.enqueue(new Callback<Resultd<CommandListDTO>>() {

			@Override
			public void onFailure(Call<Resultd<CommandListDTO>> arg0, Throwable arg1) {
				// TODO Auto-generated method stub
				clb.run(arg1);
			}

			@Override
			public void onResponse(Call<Resultd<CommandListDTO>> arg0, Response<Resultd<CommandListDTO>> arg1) {
				// TODO Auto-generated method stub

				Resultd<CommandListDTO> rd = arg1.body();
				ExternalResult<CommandListDTO> er = rd.d;
				VikoOsosException ex = er.checkStatus();
				if (ex != null) {
					clb.run(ex);
					return;
				}
				clb.run(er.Result);
				return;

			}

		});
	}

	// -------------------------------------------------------------------------

	public void OsosAktivasyon(IApplication app, IForm form, final String tesisat_no, final Param param)
	{
		app.runAsync(form, "OSOS okuma yapılıyor. 10 dk kadar sürebilir. Lütfen bekleyin...", "", null, new com.mobit.Callback(){

			@Override
			public Object run(Object obj) {
				// TODO Auto-generated method stub
				try {
					
					OsosAktivasyon(tesisat_no, param);
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return e;
				}
				return param.getResult();
			}
			
			
		}, param.clb);
	}
	
	public void OsosAktivasyon(String tesisat_no, final Param param) throws Exception {

		initService();

		param.setResult(null);
		param.serviceUsername = serviceUsername;
		param.servicePassword = servicePassword;
		param.ososUsername = ososUsername;
		param.ososPassword = ososPassword;
		param.searchText = tesisat_no;

		if (mToken == null) {

			Call<Resultd<String>> call = apiService.ServiceLogin(param.serviceUsername, param.servicePassword);
			call.enqueue(new Callback<Resultd<String>>() {

				@Override
				public void onFailure(Call<Resultd<String>> arg0, Throwable arg1) {
					// TODO Auto-generated method stub
					param.setResult(arg1);
				}

				@Override
				public void onResponse(Call<Resultd<String>> arg0, Response<Resultd<String>> arg1) {
					// TODO Auto-generated method stub
					Resultd<String> rd = arg1.body();
					ExternalResult<String> er = rd.d;
					VikoOsosException ex = er.checkStatus();
					if (ex != null) {
						param.setResult(ex);
						return;
					}
					param.token = er.Result.toString();
					//mToken = param.token;
					OsosLogin.run(param);
				}

			});
		} else {
			param.token = mToken;
			OsosLogin.run(param);
		}
		
		synchronized(param){ param.wait(10*60*1000);}
	
	}

	private com.mobit.Callback OsosLogin = new com.mobit.Callback() {

		@Override
		public Object run(Object obj) {
			// TODO Auto-generated method stub
			final Param param = (Param) obj;
			if (mUsername == null) {

				Call<Resultd<String>> call = apiService.OsosLogin(param.token, param.ososUsername, param.ososPassword);
				call.enqueue(new Callback<Resultd<String>>() {

					@Override
					public void onFailure(Call<Resultd<String>> arg0, Throwable arg1) {
						// TODO Auto-generated method stub
						param.setResult(arg1);
					}

					@Override
					public void onResponse(Call<Resultd<String>> arg0, Response<Resultd<String>> arg1) {
						// TODO Auto-generated method stub
						Resultd<?> rd = arg1.body();
						ExternalResult<?> er = rd.d;
						VikoOsosException ex = er.checkStatus();
						if (ex != null) {
							param.setResult(ex);
							return;
						}
						//mUsername = param.ososUsername;
						//ososLoginCallback.run(param);
						SearchAmr.run(param);//SearchWirings.run(param); 
					}

				});
			} else {

				SearchAmr.run(param);//SearchWirings.run(param);
			}
			return null;
		}

	};
	private com.mobit.Callback SearchWirings = new com.mobit.Callback() {

		@Override
		public Object run(Object obj) {
			// TODO Auto-generated method stub
			final Param param = (Param) obj;
			Call<Resultd<WiringDefinitionDTO>> call = apiService.SearchWirings(param.token, param.searchText);
			call.enqueue(new Callback<Resultd<WiringDefinitionDTO>>() {

				@Override
				public void onFailure(Call<Resultd<WiringDefinitionDTO>> arg0, Throwable arg1) {
					// TODO Auto-generated method stub
					param.setResult(arg1);
				}

				@Override
				public void onResponse(Call<Resultd<WiringDefinitionDTO>> arg0,
						Response<Resultd<WiringDefinitionDTO>> arg1) {
					// TODO Auto-generated method stub
					Resultd<WiringDefinitionDTO> rd = arg1.body();
					ExternalResult<WiringDefinitionDTO> er = rd.d;
					VikoOsosException ex = er.checkStatus();
					if (ex != null) {
						param.setResult(ex);
						return;
					}

					param.WiringSerno = er.Result.WiringSerno;
					SearchAmr.run(param);

				}

			});

			return null;
		}

	};
	private com.mobit.Callback SearchAmr = new com.mobit.Callback() {

		@Override
		public Object run(Object obj) {
			// TODO Auto-generated method stub
			final Param param = (Param) obj;
			//param.searchText = "73112388";
			Call<Resultd<AmrDefinitionDTO>> call = apiService.SearchAmr(param.token, param.searchText,
					ModemSearchType.ModemFromTesisatNo.getValue());
			call.enqueue(new Callback<Resultd<AmrDefinitionDTO>>() {

				@Override
				public void onFailure(Call<Resultd<AmrDefinitionDTO>> arg0, Throwable arg1) {
					// TODO Auto-generated method stub
					param.setResult(arg1);
				}

				@Override
				public void onResponse(Call<Resultd<AmrDefinitionDTO>> arg0, Response<Resultd<AmrDefinitionDTO>> arg1) {
					// TODO Auto-generated method stub
					Resultd<AmrDefinitionDTO> rd = arg1.body();
					ExternalResult<AmrDefinitionDTO> er = rd.d;
					VikoOsosException ex = er.checkStatus();
					if (ex != null) {
						param.setResult(ex);
						return;
					}

					
					param.SerNo = er.Result.SerNo;
					if(er.Result.WiringSernos.isEmpty()){
						param.setResult(new MobitException("WiringSerno gelmiyor!"));
					}
					param.WiringSerno = er.Result.WiringSernos.get(0);
					
					AddReadCommandtoAmr.run(param);
				}

			});

			return null;
		}

	};

	private com.mobit.Callback AddReadCommandtoAmr = new com.mobit.Callback() {

		@Override
		public Object run(Object obj) {
			// TODO Auto-generated method stub
			final Param param = (Param) obj;
			
			/*
			//Deneme için
			if(Globals.isDeveloping()){
				param.SerNo = 65972;
				param.WiringSerno = 48587;
			}*/
			
			Call<Resultd<String>> call = apiService.AddReadCommandtoAmr(param.token, param.ExternalCommandId,
					param.SerNo, param.WiringSerno);
			call.enqueue(new Callback<Resultd<String>>() {

				@Override
				public void onFailure(Call<Resultd<String>> arg0, Throwable arg1) {
					// TODO Auto-generated method stub
					param.setResult(arg1);
				}

				@Override
				public void onResponse(Call<Resultd<String>> arg0, Response<Resultd<String>> arg1) {
					// TODO Auto-generated method stub
					Resultd<String> rd = arg1.body();
					ExternalResult<String> er = rd.d;
					VikoOsosException ex = er.checkStatus();
					if (ex != null) {
						param.setResult(ex);
						return;
					}
					GetAmrReadCommands.run(param);
				}

			});
			return null;
		}

	};

	private com.mobit.Callback GetAmrReadCommands = new com.mobit.Callback() {

		@Override
		public Object run(Object obj) {
			// TODO Auto-generated method stub
			Param param = (Param)obj;
			param.beginTime = new Date().getTime();
			GetAmrReadCommands(param);
			return null;
		}

	};
	
	private void GetAmrReadCommands(final Param param)
	{
		
		param.trycount++;
		
		Call<Resultd<CommandListDTO>> call = apiService.GetAmrReadCommands(param.token, param.SerNo,
				param.ExternalCommandId);
		call.enqueue(new Callback<Resultd<CommandListDTO>>() {

			@Override
			public void onFailure(Call<Resultd<CommandListDTO>> arg0, Throwable arg1) {
				// TODO Auto-generated method stub
				param.setResult(arg1);
			}

			@Override
			public void onResponse(Call<Resultd<CommandListDTO>> arg0, Response<Resultd<CommandListDTO>> arg1) {
				// TODO Auto-generated method stub
				Resultd<CommandListDTO> rd = arg1.body();
				ExternalResult<CommandListDTO> er = rd.d;
				VikoOsosException ex = er.checkStatus();
				if (ex != null) {
					param.setResult(ex);
					return;
				}

				if(er.Result.Commands == null) {
					param.setResult(new MobitException("OSOS okumada hata oluştu! Tekrar deneyin."));
					return;
				}
				
				String s = "";
				CommandDTO _cmd = null;
				for(CommandDTO cmd : er.Result.Commands){
					
					CommandStatus status = cmd.getCommandStatus();
					if(status != null && status == CommandStatus.BASARILI){
						_cmd = cmd;
						break;
					}
				}
				// Okuma başarılı
				if(_cmd != null){
					
					AnswerStatus status = _cmd.getAnswerStatus();
					if(status != null && status == AnswerStatus.SIRAYA_ALINDI){
						//debug etmek için
						status = AnswerStatus.SIRAYA_ALINDI;
					}
					if(status == null || status == AnswerStatus.BASARISIZ) {
						param.setResult(new MobitException("OSOS okuma başarısız! Tekrar deneyin."));
						return;
					}
					if(status == AnswerStatus.BASARILI) {
						if(_cmd.AnswerData.isEmpty()){
							param.setResult(new MobitException("OSOS okuma verisi gelmedi! Tekrar deneyin."));
							return;
						}
						Object obj = null;
						try {
							IReadResult r = ReadResult.fromString(_cmd.AnswerData);
							obj = r;
						} catch (Exception e) {
							obj = e;
						}
						param.setResult(obj);
						return;
					}
				}
				
				if(param.canceled) return;
				
				long time = new Date().getTime();
				// 7 dk içinde cevap gelmezse hata üretilmesi
				if((time - param.beginTime) > 15*60*1000){
					param.setResult(new MobitException("OSOS okuma yapılamadı!"));
					return;
				}

				//AnswerStatus.SIRAYA_ALINDI ve AnswerStatus.ISLENIYOR durumları için tekrar istenmesi
				Timer timer = new Timer();
				timer.schedule(new TimerTask(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						GetAmrReadCommands(param);
					}
						
				}, 3000);
				
			}

		});
		
	}
	
/* 
1000020
1000024
1000081
1000088
1000103
1000106
1000121
1000134
1000207
1000213
1000215
*/

}
