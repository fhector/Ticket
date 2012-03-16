/**
 * 
 */
package main.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.filechooser.FileSystemView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railway.ticket.client.api.TicketCore;
import com.railway.ticket.client.api.bean.Contact;
import com.railway.ticket.client.api.bean.Passenger;
import com.railway.ticket.client.api.bean.PassengerOrder;
import com.railway.ticket.client.api.bean.PassengerTicket;
import com.railway.ticket.client.api.bean.Result;
import com.railway.ticket.client.api.bean.TicketOrder;
import com.railway.ticket.client.api.bean.TrainVotes;
import com.railway.ticket.client.config.Config;

/**
 * @author ChenXiaohong
 * 
 */
public class TicketBooking {
	private static final Logger logger = LoggerFactory.getLogger(TicketBooking.class);
	private TicketCore tc = null;

	/**
	 * 
	 */
	public TicketBooking() {
		tc = new TicketCore();
	}

	private TrainVotes findTrain(String trainNo, List<TrainVotes> votes) {
		for (TrainVotes vote : votes) {
			if(trainNo.equalsIgnoreCase(vote.getTrainNo())) {
				return vote;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void start() {
		Result result = new Result();
		String username = "chen_xiao_hong";//getLine("请输入用户名:");
		String password = "qwer516";//getLine("请输入密码:");
		String code = getCode(tc.getLoginCode());
		while(!result.isSuccess()) {
			result = tc.login(username, password, code);
			System.out.println(result.getMsg());
		}
		String from = Config.getStationCode("北京西");//getLine("输入乘车站:"));
		String to = Config.getStationCode("娄底");//getLine("输入到站:"));
		String date = "2012-03-18";//getLine("输入乘车日期:");
		Result votes_result = tc.queryVotes(from, to, date, "00:00--24:00", "", "QB#D#Z#T#K#QT#", "QB");
		if(votes_result.isSuccess()) {
			List<TrainVotes> voteList = (List<TrainVotes>) votes_result.getAttach();
			System.out.println("车次\t发站\t到站\t发时\t到时\t软卧\t硬卧\t软座\t硬座");
			for (TrainVotes vote : voteList) {
				System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",vote.getTrainNo(), vote.getDepartureStation(),
						vote.getArrivalStation(),vote.getDepartureTime(),vote.getArrivalTime(),
						vote.getSoftSleeper(),vote.getHardSleeper(),vote.getSoftSeat(),vote.getHardSeat()).println();
			}
			TrainVotes trainVote = findTrain(getLine("请输入预订车次:"), voteList);
			String[] seat_types = {"硬座","软座","硬卧","软卧"};
			for (String type : seat_types) {
				System.out.printf("%s、%s", Config.getSeatType(type), type).println();
			}
			String seatType = getLine("请选择席别(数字):");
			Result contact_result = tc.queryContactList(0, 10, "");
			List<Contact> contacts = new LinkedList<Contact>();
			if(contact_result.isSuccess()) {
				contacts = (List<Contact>) contact_result.getAttach();
				for (int i = 1; i <= contacts.size(); i++) {
					System.out.printf("%d、%s", i, contacts.get(i-1).getName()).println();
				}
			}
			String[] contact_index = getLine("请选择购票人,多个人之间使用逗号(,)分隔:").split("[,|，]");
			List<Passenger> passengers = new LinkedList<Passenger>();
			StringBuffer passengers_names = new StringBuffer();
			for (String index : contact_index) {
				Passenger passenger = new Passenger(contacts.get(Integer.parseInt(index)-1));
				passenger.setPassengerSeat(seatType);//席别
				passengers.add(passenger);
				passengers_names.append(passenger.getPassengerName()).append(",");
			}
			System.out.printf("输入的车次为:%s,购票人:%s", trainVote.getTrainNo(), passengers_names).println(getLine("按回车键开始预订.."));

			Result takeResult = new Result();

			while(!takeResult.isSuccess()) {
				Result requestOrder = tc.requestOrder(trainVote, "00:00--24:00");
				TicketOrder ticketOrder = (TicketOrder) requestOrder.getAttach();
				ticketOrder.setPassengers(passengers);//乘车人
				if(requestOrder.isSuccess()) {
					byte[] bytes = tc.getOrderCode();
					takeResult = tc.takeOrder(ticketOrder, getCode(bytes));
					if(takeResult.isSuccess()) {
						System.out.println(takeResult.getMsg());
						PassengerOrder takeOrder = (PassengerOrder) takeResult.getAttach();
						List<PassengerTicket> tickets = takeOrder.getTickets();
						System.out.printf("姓名\t车次\t票种\t席别\t车厢\t席位号\t票价").println();
						for (PassengerTicket ticket : tickets) {
							System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t",ticket.getPassengerName(),ticket.getTrainNo(),ticket.getTicketType(),
									ticket.getSeatType(),ticket.getCarNo(),ticket.getSeatNumber(),ticket.getFare()).println();
						}
					} else {
						System.err.println(takeResult.getMsg());
					}
				} else {
					System.err.println(requestOrder.getMsg());
				}
			}

		}
	}

	/** 将数据写入为文件后返回用户输入
	 * @param bytes
	 * @return 内容
	 */
	private String getCode(byte[] bytes) {
		try {
			File codeFile = new File(FileSystemView.getFileSystemView()
					.getHomeDirectory().getPath()
					+ "\\code.jpg");
			FileOutputStream fos = new FileOutputStream(codeFile);
			fos.write(bytes);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			System.err.println("验证码写入失败,请重新运行该程序");
		}
		return getLine("请输入验证码:");
	}

	/**
	 * 读取用户输入
	 * @param title
	 * @return 内容
	 */
	private String getLine(String title) {
		Scanner scanner = new Scanner(System.in);
		if(null != title)
			System.out.print(title);
		String line = scanner.nextLine();
		return line;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TicketBooking booking = new TicketBooking();
		booking.start();
	}


	public String readFile(String path) {
		StringBuffer content = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			String line = null;
			while((line = br.readLine()) != null)
				content.append(line).append(System.getProperty("line.separator"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}
}
