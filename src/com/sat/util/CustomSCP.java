package com.sat.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.optional.ssh.Scp;

public class CustomSCP {

	public static void main(String[] args) {
		org.apache.tools.ant.taskdefs.optional.ssh.Scp scp = new Scp();
		int portSSH = 22;
		String srvrSSH = "ssh.your.domain";
		String userSSH = "anyuser"; 
		String localFile = "C:\\localfile.txt";
		String remoteDir = "/tmp";

		scp.setPort(portSSH);
		scp.setLocalFile(localFile);
		scp.setTodir(userSSH + "@" + srvrSSH + ":" + remoteDir);
		scp.setProject(new Project());
		scp.setTrust(true);
		scp.execute();

	}

}