package hello;

public class Hello {

	public static void main(String[] args) {
		MessageProvider provider = new MessageProvider();
		System.out.println(provider.getMessage());
	}

}
