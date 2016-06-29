package DI.util;

import com.sap.smb.sbo.api.ICompany;
import com.sap.smb.sbo.api.IProductionOrders;
import com.sap.smb.sbo.api.ProductionOrders;
import com.sap.smb.sbo.api.SBOCOMConstants;
import com.sap.smb.sbo.api.SBOCOMException;
import com.sap.smb.sbo.api.SBOCOMUtil;
import com.sap.smb.sbo.api.SBOErrorMessage;

public class GetProductionOrdersToXML {
    public ICompany company;
    public IProductionOrders productionOrders;
    // set default value for connection result, 0 will mean success
    private int connectionResult = -1;
    public static void main(String[] args) 
    {
    	GetProductionOrdersToXML sapConnection = new GetProductionOrdersToXML();
        sapConnection.connect();
        sapConnection.saveProductionOrders(1,"D:\\productionOrders.xml");
        // check if connection has been established before disconnecting
        if (sapConnection.getConnectionResult() == 0)
        {
            sapConnection.disconnect();
        }    
    }
    public void saveProductionOrders(int id,String address)
    {
        try
        {
        	productionOrders = (ProductionOrders) SBOCOMUtil.getProductionOrders(company,id);
        	productionOrders.saveToFile(address);
        }
        catch(SBOCOMException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Set all connection parameters, connect to SAP Business One and initialise
     * company instance.
     */
    public void connect() 
    {  
        try 
        {
            // initialise company instance
            company = SBOCOMUtil.newCompany();
            // set database server host
            company.setServer("DESKTOP-795NCPH");
            // set company database
            company.setCompanyDB("SBODEMOUS");
            // set SAP user
            company.setUserName("manager");
            // set SAP user password
            company.setPassword("manager");
            // set SQL server version
            company.setDbServerType(SBOCOMConstants.BoDataServerTypes_dst_MSSQL2012);
            // set whether to use trusted connection to SQL server
            company.setUseTrusted(false);
            // set SAP Business One language
            company.setLanguage(SBOCOMConstants.BoSuppLangs_ln_Chinese);
            // set database user
            company.setDbUserName("sa");
            // set database user password
            company.setDbPassword("123456");
            // set license server and port
            company.setLicenseServer("DESKTOP-795NCPH:30000");
            // initialise connection
            connectionResult = company.connect();
            // if connection successful
            if (connectionResult == 0) 
            {
                System.out.println("Successfully connected to " + company.getCompanyName());
            }
            // if connection failed
            else 
            {
                // get error message fom SAP Business One Server
                SBOErrorMessage errMsg = company.getLastError();
                System.out.println(
                        "Cannot connect to server: "
                        + errMsg.getErrorMessage()
                        + " "
                        + errMsg.getErrorCode()
                );
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            connectionResult = -1;    
        }
    }
    
    /**
     * Disconnect from SAP Business One server.
     */
    public void disconnect() 
    {
        company.disconnect();
        System.out.println("Application disconnected successfully");
    }
    
    /**
     * Get connection result
     * 
     * @return 0 if success, -1 if fail
     */
    public int getConnectionResult()
    {
        return connectionResult;
    }
}
