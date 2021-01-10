using LostAnimalsAPI.Database;
using LostAnimalsAPI.Models.Auth;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using System;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Infrastructure.Seeding
{
    public static class HostExtensions
    {
        public static async Task SeedDb(this IHost host)
        {
            using (var scope = host.Services.CreateScope())
            {
                var services = scope.ServiceProvider;

                try
                {
                    var context = services.GetRequiredService<AnimalsDbContext>();
                    var userManager = services.GetRequiredService<UserManager<ApplicationUser>>();
                    var roleManager = services.GetRequiredService<RoleManager<Role>>();
                    context.Database.Migrate();

                    await Seed.SeedUsers(context, userManager);
                    await Seed.SeedSpecies(context);
                    await Seed.SeedBreed(context);
                    await Seed.SeedColors(context);
                    await Seed.SeedPosts(context);
                    await Seed.SeedComments(context);
                    await Seed.SeedShelters(context);
                }
                catch (Exception ex)
                {
                    throw ex;
                }
            }
        }
    }
}
