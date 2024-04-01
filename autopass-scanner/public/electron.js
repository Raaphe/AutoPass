const { app, BrowserWindow, Menu } = require("electron");

function createWindow() {
    const win = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            nodeIntegration: true,
        },
    });

    // Define a custom menu
    const menu = Menu.buildFromTemplate([
        {
            label: "File",
            submenu: [{ role: "quit" }],
        },
        {
            label: "Help",
            submenu: [
                {
                    label: "About",
                    click: () => {
                        console.log("About Clicked");
                    },
                },
            ],
        },
    ]);

    // Set the custom menu to the application
    Menu.setApplicationMenu(menu);
    // win.webContents.openDevTools();
    win.loadURL("http://localhost:3005");
}

app.whenReady().then(createWindow);
