using Raven.BFF;


var builder = WebApplication.CreateBuilder(args);
builder.Logging.AddFilter("Microsoft.AspNetCore.DataProtection", LogLevel.Error);
builder.Configuration.AddJsonFile("ocelot.json", optional: false, reloadOnChange: true);

var env = builder.Environment;
var config = builder.Configuration;
var services = builder.Services;

var startup = new Startup(config);

startup.ConfigureServices(services);

var app = builder.Build();

Startup.Configure(app, env);

app.Run();