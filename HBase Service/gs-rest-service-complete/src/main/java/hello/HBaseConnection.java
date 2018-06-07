package hello;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.security.User;
//import org.bigdata.hbase.spring.thrift.User;
//import org.bigdata.hbase.spring.util.SerializationUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.KeyValueUtil;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Component;
import org.apache.hadoop.hbase.client.Put;

public class HBaseConnection {

	private final String tableName = "HBase29May";
	// private TableName table1 = TableName.valueOf("Table1");
	// private String family1 = "Family1";
	// private String qualifier1 = "qualifer1";
	final String rowNamePattern = "row";
	final String columnFamilyData = "GData";
	// final String colFile = "file";
	final String colName1 = "GVisionAPI";
	final String colName2 = "MSVisionAPI";
	final String colName3 = "AWSVisionAPI";
	// final String value = "report24.csv-";
	final String value = "report24.csv-";
	Logger logger = Logger.getLogger(HBaseConnection.class);
	JSONArray JsonResp1 = null;
	String imageUrl = null;

	public HBaseConnection(JSONArray JsonResp) {
		JsonResp = this.JsonResp1;
		// System.out.println("At HBaseConnection constructor " + JsonResp);
	}

	public HbaseTemplate getHbaseTemplate() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("hbase-beans.xml");
		HbaseTemplate template = (HbaseTemplate) context.getBean("htemplate");
		logger.info("getHBaseTemplate function is executed ");
		return template;
	}

	/*
	 * public static void main(String[] args) throws Exception { HBaseConnection
	 * connection = new HBaseConnection(); HbaseTemplate hbaseTemplate =
	 * connection.getHbaseTemplate(); HBaseAdmin admin = new
	 * HBaseAdmin(hbaseTemplate.getConfiguration()); connection.createTable(admin);
	 * 
	 * // connection.insertTable(admin); connection.addData(hbaseTemplate); // User
	 * u = connection.getData(hbaseTemplate); // System.out.println("User : " + u);
	 * admin.close(); }
	 */

	public void createTable(HBaseAdmin admin) throws Exception {
		if (!admin.tableExists(tableName)) {
			// admin.disableTable(tableName);
			// admin.deleteTable(tableName);
			HTableDescriptor tableDes = new HTableDescriptor(tableName);
			HColumnDescriptor cf1 = new HColumnDescriptor(columnFamilyData);
			tableDes.addFamily(cf1);
			admin.createTable(tableDes);
			logger.info(tableName + " Table Created");
			System.out.println("Table Created");
		} else {
			System.out.println("Table Already Created");
		}
	}

	@SuppressWarnings("unused")
	public void addData(HbaseTemplate hbaseTemplate, JSONArray JsonResp, String imageUrl) {

		// System.out.println("addData JsonResp "+JsonResp);
		String respString = JsonResp.toString();
		// System.out.println("addData respString "+imageUrl);

		hbaseTemplate.execute(tableName, new TableCallback<Boolean>() {

			@SuppressWarnings("deprecation")
			public Boolean doInTable(HTableInterface table) throws Throwable {
				// for (int i = 0; i < 20; i++) {

				// Put p = new Put(Bytes.toBytes(rowNamePattern)); // RowIdentifier //Org

				Put p = new Put(Bytes.toBytes(imageUrl));
				// p.add(Bytes.toBytes(columnFamilyData),
				// Bytes.toBytes(colFile),Bytes.toBytes(value + i));//Org

				p.add(Bytes.toBytes(columnFamilyData), Bytes.toBytes(colName1), Bytes.toBytes(respString.trim()));
				// p.add(Bytes.toBytes(columnFamilyData), Bytes.toBytes(colFile1),
				// Bytes.toBytes(value + i));
				table.put(p);
				logger.info("GVision Data inserted in table");

				// Put p1 = new Put(Bytes.toBytes(imageToAnalyze));
				// p1.add(Bytes.toBytes(columnFamilyData), Bytes.toBytes(colName2),
				// Bytes.toBytes(MSJsonResp));
				// table.put(p1);
				// logger.info("MSVision Data inserted in table");

				System.out.println("data inserted");
				return new Boolean(true);
			}
		});
	}

	@SuppressWarnings("unused")
	public void addMSData(HbaseTemplate hbaseTemplate, String imageToAnalyze, String MSJsonResp) {

		hbaseTemplate.execute(tableName, new TableCallback<Boolean>() {
			@SuppressWarnings("deprecation")
			public Boolean doInTable(HTableInterface table) throws Throwable {

				Put p1 = new Put(Bytes.toBytes(imageToAnalyze));
				p1.add(Bytes.toBytes(columnFamilyData), Bytes.toBytes(colName2), Bytes.toBytes(MSJsonResp));
				table.put(p1);
				logger.info("MSVision Data inserted in table");
				System.out.println("data inserted");
				return new Boolean(true);
			}
		});
	}

	@SuppressWarnings("unused")
	public void addAwsData(HbaseTemplate hbaseTemplate, String photoId, String LDetectResp) {

		hbaseTemplate.execute(tableName, new TableCallback<Boolean>() {
			@SuppressWarnings("deprecation")
			public Boolean doInTable(HTableInterface table) throws Throwable {
				Put p1 = new Put(Bytes.toBytes(photoId));
				p1.add(Bytes.toBytes(columnFamilyData), Bytes.toBytes(colName3), Bytes.toBytes(LDetectResp));
				table.put(p1);
				logger.info("AWSVision Data inserted in table");
				System.out.println("data inserted");
				return new Boolean(true);
			}
		});
	}
}
