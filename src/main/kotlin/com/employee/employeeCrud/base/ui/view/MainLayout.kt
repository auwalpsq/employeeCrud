package com.employee.employeeCrud.base.ui.view

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.avatar.AvatarVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.menubar.MenuBarVariant
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem
import com.vaadin.flow.router.Layout
import com.vaadin.flow.server.menu.MenuConfiguration
import com.vaadin.flow.server.menu.MenuEntry
import com.vaadin.flow.theme.lumo.LumoUtility.*
import jakarta.annotation.security.PermitAll
import java.util.function.Consumer
import com.vaadin.flow.spring.security.AuthenticationContext
@PermitAll
@Layout
class MainLayout internal constructor(private val authenticationContext: AuthenticationContext) : AppLayout() {
    init {
        primarySection = Section.DRAWER
        addToDrawer(createHeader(), Scroller(createSideNav()), createUserMenu())
    }

    private fun createHeader(): Div {
        // TODO Replace with real application logo and name
        val appLogo = VaadinIcon.CUBES.create()
        appLogo.addClassNames(TextColor.PRIMARY, IconSize.LARGE)

        val appName = Span("Employee Management System")
        appName.addClassNames(FontWeight.SEMIBOLD, FontSize.LARGE)

        val header = Div(appLogo, appName)
        header.addClassNames(
            Display.FLEX,
            Padding.MEDIUM,
            Gap.MEDIUM,
            AlignItems.CENTER
        )
        return header
    }

    private fun createSideNav(): SideNav {
        val nav = SideNav()
        nav.addClassNames(Margin.Horizontal.MEDIUM)
        MenuConfiguration.getMenuEntries()
            .forEach(Consumer { entry: MenuEntry? -> nav.addItem(createSideNavItem(entry!!)) })
        return nav
    }

    private fun createSideNavItem(menuEntry: MenuEntry): SideNavItem {
        if (menuEntry.icon() != null) {
            return SideNavItem(menuEntry.title(), menuEntry.path(), Icon(menuEntry.icon()))
        } else {
            return SideNavItem(menuEntry.title(), menuEntry.path())
        }
    }

    private fun createUserMenu(): Component {
        // TODO Replace with real user information and actions
        val avatar = Avatar("Auwal Auwal")
        avatar.addThemeVariants(AvatarVariant.LUMO_XSMALL)
        avatar.addClassNames(Margin.Right.SMALL)
        avatar.colorIndex = 5

        val userMenu = MenuBar()
        userMenu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE)
        userMenu.addClassNames(Margin.MEDIUM)

        val userMenuItem = userMenu.addItem(avatar)
        userMenuItem.add("Auwal Usman")
        userMenuItem.subMenu.addItem("View Profile")
        userMenuItem.subMenu.addItem("Manage Settings")
        userMenuItem.subMenu.addItem("Logout", {event -> authenticationContext.logout()})

        return userMenu
    }
}