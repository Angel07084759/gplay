public class JavaTest
{
	public static void main(String[] args)
	{
		String[] test = new String[]
		{
			"u2",
			"us3",
			"use4",
			"user5",
			"userA6",
			"Useraa7",
			"usernam8",
			"UserName9",
			"userName10",
			"userNamex11",
			"userNamexy12",
			"userNameLon13",
			"userNameLong14",
			"userNameLongx15",
			"userNameLongXL16",
			"user1",
			"use r1",
			"a1",
			"_user1",
			"1user",
			"a",
			"1",
			"",
			"a.c",
			"a_c",
			"1234567890",
			"3898333498",
			"123456789",
			"01234567890"
		};
		for (int i = 0; i < test.length; i++)
		{
			System.out.printf("%-3s%s%n",(test[i].matches(".*[\\w\\W\\d].{4,14}") 
			//&& !test[i].matches("[\\s]")
			? "*" : "" ) 
			, test[i]);
		}
	}	
	public static void main0(String[] args)
	{
		String[] test = new String[]
		{
			"u2",
			"us3",
			"use4",
			"user5",
			"userA6",
			"Useraa7",
			"usernam8",
			"UserName9",
			"userName10",
			"userNamex11",
			"userNamexy12",
			"userNameLon13",
			"userNameLong14",
			"userNameLongx15",
			"userNameLongXL16",
			"user1",
			"use r1",
			"a1",
			"_user1",
			"1user",
			"a",
			"1",
			"",
			"a.c",
			"a_c",
			"1234567890",
			"3898333498",
			"123456789",
			"01234567890"
		};
		for (int i = 0; i < test.length; i++)
		{
			System.out.printf("%-3s%s%n",(test[i].matches(".*^[a-zA-Z].{5,14}") ? "*" : "" ) , test[i]);
		}
	}
	
	/*
	must start with a letter
	can have numeric values
	space is not allowed
	if phone#:  length = 10
	else if id: length = [3-15]
	*/
	static boolean isValidUserId(String id)
	{
		return id.matches(".*^[a-zA-Z].{2,14}") && !id.matches(".*[\\W_].+") || id.matches("[0-9]{10}");
	}
	
	
	
}