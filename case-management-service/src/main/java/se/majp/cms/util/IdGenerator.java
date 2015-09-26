package se.majp.cms.util;

import java.util.Random;

public class IdGenerator
{
	private char[] symbols;
	private int length;
	private final Random random = new Random();

	private IdGenerator()
	{
		setSymbols('0', 'z');
		this.length = 16;
	}

	private void setLength(int length)
	{
		this.length = length;
	}

	private void setSymbols(char from, char to)
	{
		StringBuilder symbols = new StringBuilder();
		for (char ch = from; ch <= to; ch++)
		{
			if (Character.isLetter(ch) || Character.isDigit(ch))
			{
				symbols.append(ch);
			}
		}

		this.symbols = symbols.toString().toCharArray();
	}

	public String getNextId()
	{
		final char[] buffer = new char[length];

		for (int i = 0; i < length; i++)
		{
			buffer[i] = symbols[random.nextInt(symbols.length)];
		}

		return new String(buffer);
	}

	public static IdGeneratorBuilder getBuilder()
	{
		return new IdGeneratorBuilder();
	}

	public static final class IdGeneratorBuilder
	{
		private final IdGenerator generator = new IdGenerator();

		public IdGeneratorBuilder characters(char from, char to)
		{
			generator.setSymbols(from, to);
			return this;
		}

		public IdGeneratorBuilder length(int length)
		{
			generator.setLength(length);
			return this;
		}

		public IdGenerator build()
		{
			return generator;
		}
	}
}
