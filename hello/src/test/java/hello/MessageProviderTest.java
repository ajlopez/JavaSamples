package hello;

import static org.junit.Assert.*;

import org.junit.Test;

public class MessageProviderTest {

	@Test
	public void messageIsNotNull() {
		MessageProvider provider = new MessageProvider();
		assertNotNull(provider.getMessage());
	}
}
