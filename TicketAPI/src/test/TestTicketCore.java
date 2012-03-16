package test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

import javax.swing.filechooser.FileSystemView;

import com.railway.ticket.client.api.TicketCore;
import com.railway.ticket.client.api.bean.Contact;
import com.railway.ticket.client.api.bean.Result;

public class TestTicketCore {
	public static String getCode(byte[] bytes) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(FileSystemView.getFileSystemView().getHomeDirectory().getPath() +"\\code.jpg"));
			fos.write(bytes);
			fos.flush();
			fos.close();
		} catch (Exception e) {
		}
		Scanner scanner = new Scanner(System.in);
		String code = scanner.nextLine();
		
		return code;
	}
	
	
	public static void main(String[] args) {
//		Config.HOST = "http://localhost:8080/service";
//		HttpClient client = HttpUtils.createClient();
//		HttpUtils.get(client, "/agent");
//		if(true)
//			return;
		
		TicketCore tc = new TicketCore();
		//: 测试
		
		byte[] bytes = tc.getLoginCode();
		Result login = tc.login("chen_xiao_hong", "qwer516", getCode(bytes));
		
		if(login.isSuccess()) {
//			System.out.println(tc.changePassword("qwert516", "qwer516").getMsg());
			
			
			
			
			
			
			
			
			
			Contact contact = new Contact("蔡颖翔", "F", "1991-11-18", "1", "432524199111181627", "18210543345");
			Result cre_result = tc.createContact(contact);
			System.out.println(cre_result.getMsg());
//			Result cat_result =  tc.queryContactList(0, 10, "");
//			List<Contact> contacts = (List<Contact>) cat_result.getAttach();
//			System.out.println(contacts.size());
//			contact = contacts.get(0);
//			System.out.println(contact.getName());
//			System.out.println(contact.getCardType());
//			System.out.println(contact.getCardNo());
//			System.out.println(contact.getPsgTypeCode());
//			tc.deleteContact(contact.getName(), contact.getCardType(), contact.getCardNo(), contact.getPsgTypeCode());
//			
//			Result votes = tc.queryVotes("BXP", "LDQ", "2012-03-07", "00:00--24:00", "", "QB#D#Z#T#K#QT#", "QB");
//			List<TrainVotes> voteList = (List<TrainVotes>) votes.getAttach();
//
//			TrainVotes vote = voteList.get(1);
//
//			Result stopInfoResult = tc.queryTrainStopInfo(vote.getTrainCode(), vote.getDepartureStationCode(), 
//					vote.getArrivalStationCode(), vote.getTravelDate());
//			JSONArray stopArr = (JSONArray) stopInfoResult.getAttach();
//			for (Object stopInfo : stopArr) {
//				JSONObject json = (JSONObject) stopInfo;
//				System.out.println(json.getString("station_no") + "\t" +  json.getString("station_name") + "\t" +  json.getString("arrive_time") +
//						"\t" + json.getString("start_time") + "\t" + json.getString("stopover_time"));
//			}
//			
//			
//			Result requestOrder = tc.requestOrder(voteList.get(0), "00:00--24:00");
//			
//			if(requestOrder.isSuccess()) {
//				TicketOrder ticketOrder = (TicketOrder) requestOrder.getAttach();
//				
//				Result takeResult = new Result();
//				while(!takeResult.isSuccess()) {
//					bytes = tc.getOrderCode();
//					takeResult = tc.takeOrder(ticketOrder, getCode(bytes));
//					if(takeResult.isSuccess()) {
//						PassengerOrder takeOrder = (PassengerOrder) takeResult.getAttach();
//						
//						tc.queryNotCompleteOrder();
//						
//						//							Result cancelResult = tc.cancelOrder(takeOrder.getSequenceNo(), takeOrder.getToken(), takeOrder.getBatchNo(), takeOrder.getCancelFlag());
//						//							System.out.println(cancelResult.getMsg());
//					} else {
//						System.err.println(takeResult.getMsg());
//					}
//				}
//			}
			
			tc.logout();
		} else {
			System.err.println(login.getMsg());
		}
		
		/*** 
		JSONObject login = tc.login("chen_xiao_hong", "qwer516", code);
		if(ResultResolve.isSuccess(login)) {
			JSONObject votes = tc.queryVotes("BXP", "LDQ", "2012-03-06", "00:00--24:00", "", "QB#D#Z#T#K#QT#", "QB");
			
			TrainVotes vote = JSONObject.toJavaObject((JSONObject)votes.getJSONArray("attach").get(0), TrainVotes.class);
			
			JSONObject requestOrder =  tc.requestOrder(vote, "2012-03-06", "00:00--24:00");
			TicketOrder order = JSONObject.toJavaObject((JSONObject)requestOrder.get("attach"), TicketOrder.class);
			
			try {
				bytes = tc.getOrderCode();
				FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\ChenXiaohong\\Desktop\\order.png"));
				fos.write(bytes);
				fos.flush();
				fos.close();
			} catch (Exception e) {
			}
			code = scanner.nextLine();
			
			JSONObject takeOrderJSON =  tc.takeOrder(order, code);
			PassengerOrder takeOrder = JSONObject.toJavaObject((JSONObject)takeOrderJSON.get("attach"), PassengerOrder.class);
			
			tc.cancelOrder(takeOrder.getSequenceNo(), takeOrder.getToken(), takeOrder.getBatchNo(), takeOrder.getCancelFlag());
//			System.out.println(tc.queryTrainStopInfo("2400000T610L", "BXP", "LDQ", "2012-03-06"));
		} else {
			System.out.println(login);
		}
		
		*/
	}
}