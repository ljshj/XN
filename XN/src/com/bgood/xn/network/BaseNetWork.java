package com.bgood.xn.network;import java.io.ByteArrayInputStream;import java.io.ByteArrayOutputStream;import java.io.DataInputStream;import java.io.DataOutputStream;import java.io.IOException;import java.io.UnsupportedEncodingException;import org.json.JSONException;import org.json.JSONObject;import android.util.Log;import com.bgood.xn.utils.FormatTransfer;import com.bgood.xn.utils.LogUtils;public class BaseNetWork{	public static final String START = "<<<";	public static final String END = ">>>";	public static int START_LENGTH = START.getBytes().length;	public static int END_LENGTH = END.getBytes().length;	private int length;	private int messageType;	private int sessionID; // 只有request才有	private short messageClass;	private ReturnCode returnCode = ReturnCode.RETURNCODE_NONE;	private int bodyLength = 0;	private JSONObject body;	private String strJson;	//返回json字符串	public String getStrJson() {		return strJson;	}	public void setStrJson(String strJson) {		this.strJson = strJson;	}	public int getLength()	{		return length;	}	public int getMessageType()	{		return messageType;	}		public JSONObject getBody()	{		return body;	}	public void setBody(JSONObject body)	{		this.body = body;	}		public short getMessageClass()	{		return messageClass;	}	public void setMessageClass(short messageClass)	{		this.messageClass = messageClass;	}	public ReturnCode getReturnCode()	{		return returnCode;	}	public void setReturnCode(ReturnCode returnCode)	{		this.returnCode = returnCode;	}	public void setMessageType(int messageType)	{		this.messageType = messageType;	}	public int getSessionID()	{		return sessionID;	}	public void setSessionID(int sessionID)	{		this.sessionID = sessionID;	}	public int getBodyLength()	{		return bodyLength;	}		/**	 * 获取总长度	 */	public int GetMeasureLength()	{		byte[] start = null;		byte[] end = null;		try		{			start = START.getBytes("utf-8");			end = END.getBytes("utf-8");		} catch (UnsupportedEncodingException e)		{			e.printStackTrace();		}		/* 计算body长度 */		if (getBody() != null)			bodyLength = getBody().toString().getBytes().length;		/* 计算总长度 */		length = 4 * 4 + 2 + bodyLength + start.length + end.length;		return length;	};			/**	 * 解析服务器返回的IM数据	 * @param bs	 * @param length	 * @throws JSONException	 */	public static BaseNetWork parseIM(byte[] bs, int length) throws JSONException	{		BaseNetWork f = new BaseNetWork();		ByteArrayInputStream bis = new ByteArrayInputStream(bs);		DataInputStream dis = new DataInputStream(bis);		try		{			byte[] b_int = new byte[4];			byte[] b_short = new byte[2];			byte[] b_start = new byte[3];			dis.read(b_start);			String start = new String(b_start, "utf-8");			if (start == null || !start.equals(START))				return null;			dis.read(b_int);			f.length = FormatTransfer.lBytesToInt(b_int);			dis.read(b_int);			f.messageType = FormatTransfer.lBytesToInt(b_int);			dis.read(b_int);			f.sessionID = FormatTransfer.lBytesToInt(b_int);			dis.read(b_short);			f.messageClass = FormatTransfer.lBytesToShort(b_short);			dis.read(b_short);			short returnCode = FormatTransfer.lBytesToShort(b_short);			switch (returnCode)			{			case 0:				f.setReturnCode(ReturnCode.RETURNCODE_OK);				break;			case -1:				f.setReturnCode(ReturnCode.RETURNCODE_FAIL);				break;			case 101:				f.setReturnCode(ReturnCode.RETURNCODE_MESSAGETYPE_ERROR);				break;			case 102:				f.setReturnCode(ReturnCode.RETURNCODE_DATA_ERROR);				break;			case 103:				f.setReturnCode(ReturnCode.RETURNCODE_SESSION_OVER);				break;			case 104:				f.setReturnCode(ReturnCode.RETURNCODE_CHAT_DISCONNECT);				break;			default:				f.setReturnCode(ReturnCode.RETURNCODE_NONE);			}			dis.read(b_int);			f.bodyLength = FormatTransfer.lBytesToInt(b_int);			byte[] bsbody = new byte[f.bodyLength];			dis.read(bsbody, 0, bsbody.length);			String strbody = new String(bsbody, "utf-8");			if (strbody != null)			{				JSONObject jsonObject = new JSONObject(strbody);				f.setBody(jsonObject);			}			byte[] end_sign = new byte[END_LENGTH];			dis.read(end_sign);			String s = new String(end_sign, "utf-8");			if (s == null || !s.equals(END))				return null;		} catch (IOException e)		{			e.printStackTrace();			return null;		} finally		{			try			{				dis.close();			} catch (IOException e)			{				e.printStackTrace();			}			try			{				bis.close();			} catch (IOException e)			{				e.printStackTrace();			}		}		return f;	}	/**	 * 	 * 解析服务器返回的数据	 * @param bs	 * @param length	 * @return	 * @throws JSONException	 */	public static BaseNetWork parseB(byte[] bs, int length) throws JSONException	{		BaseNetWork f = new BaseNetWork();		ByteArrayInputStream bis = new ByteArrayInputStream(bs);		DataInputStream dis = new DataInputStream(bis);		try		{			String res = new String(bs);			Log.i("parseB-- >", res);			byte[] b_int = new byte[4];			byte[] b_short = new byte[2];			dis.read(b_int);			f.length = FormatTransfer.lBytesToInt(b_int);			dis.read(b_int);			f.messageType = FormatTransfer.lBytesToInt(b_int);			dis.read(b_int);			f.sessionID = FormatTransfer.lBytesToInt(b_int);			dis.read(b_short);			f.messageClass = FormatTransfer.lBytesToShort(b_short);			dis.read(b_short);			short returnCode = FormatTransfer.lBytesToShort(b_short);			switch (returnCode)			{			case 0:				f.setReturnCode(ReturnCode.RETURNCODE_OK);				break;			case -1:				f.setReturnCode(ReturnCode.RETURNCODE_FAIL);				break;			case 101:				f.setReturnCode(ReturnCode.RETURNCODE_MESSAGETYPE_ERROR);				break;			case 102:				f.setReturnCode(ReturnCode.RETURNCODE_DATA_ERROR);				break;			case 103:				f.setReturnCode(ReturnCode.RETURNCODE_SESSION_OVER);				break;			case 104:				f.setReturnCode(ReturnCode.RETURNCODE_CHAT_DISCONNECT);				break;			default:				f.setReturnCode(ReturnCode.RETURNCODE_NONE);			}			dis.read(b_int);			f.bodyLength = FormatTransfer.lBytesToInt(b_int);			byte[] bsbody = new byte[f.bodyLength];			dis.read(bsbody, 0, bsbody.length);			String strbody = new String(bsbody, "utf-8");						LogUtils.i("------------------------------服务器返回的数据"+strbody);						f.setStrJson(strbody);			if (strbody != null && !strbody.equals(""))			{				JSONObject jsonObject = new JSONObject(strbody);				f.setBody(jsonObject);			}		} catch (IOException e)		{			e.printStackTrace();			return null;		} finally		{			try			{				dis.close();			} catch (IOException e)			{				e.printStackTrace();			}			try			{				bis.close();			} catch (IOException e)			{				e.printStackTrace();			}		}		return f;	}	/**	 * 把请求的数据转换成字节流	 * @return	 */	public byte[] toBytes()	{		ByteArrayOutputStream bos = new ByteArrayOutputStream();		DataOutputStream dos = new DataOutputStream(bos);		try		{			/* 开始写数据 */			dos.write(START.getBytes("utf-8"));			byte[] length_net = FormatTransfer.toLH(length);			dos.write(length_net);			byte[] messageType_net = FormatTransfer.toLH(messageType);			dos.write(messageType_net);			byte[] seessionID_net = FormatTransfer.toLH(sessionID);			dos.write(seessionID_net);			byte[] messageClass_net = FormatTransfer.toLH(messageClass);			dos.write(messageClass_net);			byte[] bodyLength_net = FormatTransfer.toLH(bodyLength);			dos.write(bodyLength_net);			if (getBody() != null)				dos.write(getBody().toString().getBytes());			dos.write(END.getBytes("utf-8"));			byte[] bs = bos.toByteArray();			return bs;		} catch (IOException e)		{			e.printStackTrace();			return null;		} finally		{			if (dos != null)				try				{					dos.close();				} catch (IOException e)				{					e.printStackTrace();				} finally				{					dos = null;				}			if (bos != null)				try				{					bos.close();				} catch (IOException e)				{					e.printStackTrace();				} finally				{					bos = null;				}		}	}	/**	 * 发送给服务端	 * @param dos	 * @throws IOException	 */	public void send(DataOutputStream dos) throws IOException	{		if (dos == null)			return;		GetMeasureLength();		byte[] bs = toBytes();		dos.write(bs);		dos.flush();	}	public enum ReturnCode	{		RETURNCODE_NONE, RETURNCODE_OK, RETURNCODE_FAIL, RETURNCODE_MESSAGETYPE_ERROR, RETURNCODE_DATA_ERROR, RETURNCODE_SESSION_OVER, RETURNCODE_CHAT_DISCONNECT, RETURNCODE_MAX;	}}