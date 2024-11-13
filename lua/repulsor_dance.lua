rep = peripheral.find("opencu:repulsor")

api = rep.getDeviceApi()

t = 0
f = 6.28 / 120

while true do
    api.setDirectionBlend(math.sin(t * f) / 2 + 0.5)
    t = t + 1
    os.sleep(0.05)
end
