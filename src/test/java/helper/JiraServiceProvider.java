package helper;
;
import net.rcarz.jiraclient.*;
import org.apache.http.impl.client.BasicCredentialsProvider;

import java.io.File;


public class JiraServiceProvider {
    public JiraClient jira;
    public String project;
    public JiraServiceProvider(String jiraurl,String username, String Password,String project){

        BasicCredentials creds= new  BasicCredentials(username,Password);
        jira = new JiraClient(jiraurl,creds);
        this.project=project;
    }
    public void CreateJiraTicket(String issuetype, String summary, String description) throws JiraException {

        Issue.FluentCreate fluentcreate= jira.createIssue(project,issuetype);
        fluentcreate.field(Field.SUMMARY,summary);
        fluentcreate.field(Field.ISSUE_TYPE,issuetype);
        fluentcreate.field(Field.DESCRIPTION,description);
        Issue newissue= fluentcreate.execute();
        System.out.println("new issue has been created with Jira ID" + newissue);

    }
}
