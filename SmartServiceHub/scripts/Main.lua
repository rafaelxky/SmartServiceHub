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
        counter = counter + 1
        print("Home accessed - " .. counter)
    end
}

register_event {
    event = "onAppStartup",
    func = function ()
        print("app startup ---------------------------------------------------------------------------------------------------")
    end
}

print(Network:httpGet("http://localhost:8080/home"))




