register_event {
    event = "onAdminCreate",
    func = function(user)
        print("New admin created: " .. (user.username or "unknown") .. " with role: " .. (user.role or "unknown"))
    end
}

counter = 0

register_event {
    event = "onHomeAccessed",
    func = function()
        print("Home accessed - " .. counter)
        counter = counter + 1
    end
}

register_event {
    event = "onAppStartup",
    func = function ()
        print("app startup ---------------------------------------------------------------------------------------------------")
    end
}




