package com.employee.employeeCrud.base.ui.view

import com.employee.employeeCrud.base.ui.component.ViewToolbar
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Main
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.lumo.LumoUtility
import jakarta.annotation.security.PermitAll

/**
 * This view shows up when a user navigates to the root ('/') of the application.
 */
@PermitAll
@Route
@Menu(order = 0.0, icon = "vaadin:home", title = "Home")
class MainView internal constructor() : Main() {
    // TODO Replace with your own main view.
    init {
        addClassName(LumoUtility.Padding.MEDIUM)
        add(ViewToolbar("Home"))
        add(Div("Please select a view from the menu on the left."))
    }

    companion object {
        /**
         * Navigates to the main view.
         */
        fun showMainView() {
            UI.getCurrent().navigate<MainView?>(MainView::class.java)
        }
    }
}