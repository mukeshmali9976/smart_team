package com.techmali.smartteam.models;

import java.io.Serializable;

/**
 * Created by Gaurav on 7/28/2017.
 */

public class SyncExpense implements Serializable {

    private String created_by;
    private String local_expance_id;
    private String company_id;
    private String local_project_id;
    private String expance_id;
    private String created_on;
    private String status_id;
    private String payout_status;
    private String task_id;
    private String amount;
    private String title;
    private String description;
    private String local_user_id;
    private String project_id;
    private String updated_by;
    private String updated_on;
    private String user_id;
    private String local_task_id;

    public String getCreated_by ()
    {
        return created_by;
    }

    public void setCreated_by (String created_by)
    {
        this.created_by = created_by;
    }

    public String getLocal_expance_id ()
    {
        return local_expance_id;
    }

    public void setLocal_expance_id (String local_expance_id)
    {
        this.local_expance_id = local_expance_id;
    }

    public String getCompany_id ()
    {
        return company_id;
    }

    public void setCompany_id (String company_id)
    {
        this.company_id = company_id;
    }

    public String getLocal_project_id ()
    {
        return local_project_id;
    }

    public void setLocal_project_id (String local_project_id)
    {
        this.local_project_id = local_project_id;
    }

    public String getExpance_id ()
    {
        return expance_id;
    }

    public void setExpance_id (String expance_id)
    {
        this.expance_id = expance_id;
    }

    public String getCreated_on ()
    {
        return created_on;
    }

    public void setCreated_on (String created_on)
    {
        this.created_on = created_on;
    }

    public String getStatus_id ()
    {
        return status_id;
    }

    public void setStatus_id (String status_id)
    {
        this.status_id = status_id;
    }

    public String getPayout_status ()
    {
        return payout_status;
    }

    public void setPayout_status (String payout_status)
    {
        this.payout_status = payout_status;
    }

    public String getTask_id ()
    {
        return task_id;
    }

    public void setTask_id (String task_id)
    {
        this.task_id = task_id;
    }

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getLocal_user_id ()
    {
        return local_user_id;
    }

    public void setLocal_user_id (String local_user_id)
    {
        this.local_user_id = local_user_id;
    }

    public String getProject_id ()
    {
        return project_id;
    }

    public void setProject_id (String project_id)
    {
        this.project_id = project_id;
    }

    public String getUpdated_by ()
    {
        return updated_by;
    }

    public void setUpdated_by (String updated_by)
    {
        this.updated_by = updated_by;
    }

    public String getUpdated_on ()
    {
        return updated_on;
    }

    public void setUpdated_on (String updated_on)
    {
        this.updated_on = updated_on;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getLocal_task_id ()
    {
        return local_task_id;
    }

    public void setLocal_task_id (String local_task_id)
    {
        this.local_task_id = local_task_id;
    }
}
