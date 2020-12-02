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

                    try
                    {
                        await Seed.SeedUsers(context, userManager);
                    }
                    catch (Exception ex)
                    {
                        throw ex;
                    }

                    try
                    {
                        await Seed.SeedSpecies(context);
                    }
                    catch (Exception ex)
                    {
                        throw ex;
                    }

                    try
                    {
                        await Seed.SeedBreed(context);
                    }
                    catch (Exception ex)
                    {
                        throw ex;
                    }

                    try
                    {
                        await Seed.SeedColors(context);
                    }
                    catch (Exception ex)
                    {
                        throw ex;
                    }

                    try
                    {
                        await Seed.SeedPosts(context);
                    }
                    catch (Exception ex)
                    {
                        throw ex;
                    }

                    try
                    {
                        await Seed.SeedComments(context);
                    }
                    catch (Exception ex)
                    {
                        throw ex;
                    }
                }
                catch (Exception ex)
                {
                    throw ex;
                }
            }
        }
    }
}
