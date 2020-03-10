package com.br.ott.common.jackson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.util.FileCopyUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class UTF8StringHttpMessageConverter extends AbstractHttpMessageConverter<String>{
	 public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	  private final List<Charset> availableCharsets;
	  private boolean writeAcceptCharset = true;

	public UTF8StringHttpMessageConverter() {
	    super(new MediaType[] { new MediaType("text", "plain", DEFAULT_CHARSET), MediaType.ALL });
	    this.availableCharsets = new ArrayList(Charset.availableCharsets().values());
	  }

	  public void setWriteAcceptCharset(boolean writeAcceptCharset)
	  {
	    this.writeAcceptCharset = writeAcceptCharset;
	  }

	  @Override
	  public boolean supports(Class<?> clazz)
	  {
	    return String.class.equals(clazz);
	  }
	  
	  @Override 
	  protected String readInternal(Class clazz, HttpInputMessage inputMessage) throws IOException
	  {
	    MediaType contentType = inputMessage.getHeaders().getContentType();
	    Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : DEFAULT_CHARSET;
	    return FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
	  }
	  
	  @Override
	  protected Long getContentLength(String s, MediaType contentType)
	  {
	    if ((contentType != null) && (contentType.getCharSet() != null)) {
	      Charset charset = contentType.getCharSet();
	      try {
	        return Long.valueOf(s.getBytes(charset.name()).length);
	      }
	      catch (UnsupportedEncodingException ex)
	      {
	        throw new InternalError(ex.getMessage());
	      }
	    }

	    return null;
	  }

	  @Override
	  protected void writeInternal(String s, HttpOutputMessage outputMessage)
	    throws IOException
	  {
	    if (this.writeAcceptCharset) {
	      outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
	    }
	    MediaType contentType = outputMessage.getHeaders().getContentType();
	    Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : DEFAULT_CHARSET;
	    FileCopyUtils.copy(s, new OutputStreamWriter(outputMessage.getBody(), charset));
	  }

	  protected List<Charset> getAcceptedCharsets()
	  {
	    return this.availableCharsets;
	  }
}
