/*
 * Copyright (c) Interactive Information R & D (I2RD) LLC.
 * All Rights Reserved.
 *
 * This software is confidential and proprietary information of
 * I2RD LLC ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered
 * into with I2RD.
 */

package com.example.app.ui.company;

import com.example.app.model.company.Company;
import com.example.app.model.user.User;
import com.example.app.model.user.UserDAO;
import com.example.app.support.AppUtil;
import com.example.app.ui.ApplicationFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import javax.annotation.Nullable;

import net.proteusframework.ui.management.nav.NavigationAction;
import net.proteusframework.ui.miwt.component.composite.editor.PropertyViewer;
import net.proteusframework.ui.miwt.util.CommonActions;

/**
 * PropertyViewer for {@link Company}
 *
 * @author Alan Holt (aholt@venturetech.net)
 * @since 6/28/16 10:14 AM
 */
@Configurable
public class CoachingEntityPropertyViewer extends PropertyViewer
{
    @Autowired
    private UserDAO _userDAO;

    /**
     * Instantiates a new company property viewer.
     */
    public CoachingEntityPropertyViewer()
    {
        super();

        addClassName("company-view");
    }

    @Override
    public void init()
    {
        super.init();
        super.lazyInit();

        NavigationAction editAction = CommonActions.EDIT.navAction();
        editAction.configure().toPage(ApplicationFunctions.Company.EDIT)
            .withSourceComponent(this)
            .usingCurrentURLData();
        editAction.setTarget(this, "close");

        NavigationAction backAction = CommonActions.BACK.navAction();
        backAction.configure().toReturnPath(ApplicationFunctions.Company.MANAGEMENT)
            .withSourceComponent(this)
            .usingCurrentURLData();
        backAction.setTarget(this, "close");

        setPersistenceActions(editAction, backAction);
    }

    @Nullable
    @Override
    public CoachingEntityValueViewer getValueViewer()
    {
        return (CoachingEntityValueViewer)super.getValueViewer();
    }

    /**
     * Configure company property viewer.
     *
     * @param value the value
     *
     * @return the company property viewer
     */
    public CoachingEntityPropertyViewer configure(Company value)
    {
        final User currentUser = _userDAO.getAssertedCurrentUser();
        if(!AppUtil.userHasAdminRole(currentUser))
            throw new IllegalArgumentException(String.format("User %s does not have the correct role to view this page",
                currentUser.getId()));
        if(value == null)
        {
            throw new IllegalArgumentException("Unable to determine Development Provider");
        }

        CoachingEntityValueViewer viewer = new CoachingEntityValueViewer();
        viewer.setCoaching(value);
        setValueViewer(viewer);
        return this;
    }
}
