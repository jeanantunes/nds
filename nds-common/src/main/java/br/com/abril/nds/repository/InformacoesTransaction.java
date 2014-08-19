package br.com.abril.nds.repository;

public class InformacoesTransaction {

	public static String getInfo() {

		String stackInfo = "erro no chamador.";
		String thisClassName = "br.com.abril.nds.repository.InformacoesTransaction";
		Thread thread = Thread.currentThread();
		StackTraceElement[] stackElements = thread.getStackTrace();

		for (StackTraceElement ste : stackElements) {

			if (!ste.getClassName().equals("java.lang.Thread")
					&& !ste.getClassName().equals(thisClassName)
					&& !ste.getClassName().equals("br.com.abril.nds.repository.AbstractRepositoryModel")
					&& ( ste.getClassName().indexOf("Service") > -1 || ste.getClassName().indexOf("Repository") > -1)) {

				String fileName = ste.getFileName() != null ? ste.getFileName() : "Unknown";
				StringBuilder sb = new StringBuilder(ste.getClassName());
				sb.append('.');
				sb.append(ste.getMethodName());
				sb.append('(');
				sb.append(fileName);
				sb.append(':');
				sb.append(ste.getLineNumber());
				sb.append(')');
				stackInfo = sb.toString();

				break;
			}
		}

		return stackInfo;
	}

}