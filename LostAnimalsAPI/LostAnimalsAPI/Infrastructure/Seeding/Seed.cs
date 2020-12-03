using LostAnimalsAPI.Database;
using LostAnimalsAPI.Models;
using LostAnimalsAPI.Models.Auth;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using System;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Infrastructure.Seeding
{
    public class Seed
    {

        public static async Task SeedUsers(AnimalsDbContext context, UserManager<ApplicationUser> userManager) 
        {
            if (!context.Users.Any())
            {
                var user = new ApplicationUser
                {
                    FirstName = "Nick",
                    LastName = "Basso",
                    Email = "nick@mail.com",
                    UserName = "nick@mail.com"
                };

                await userManager.CreateAsync(user, "nick1234");

                user = new ApplicationUser
                {
                    FirstName = "Alex",
                    LastName = "Mayson",
                    Email = "alex@mail.com",
                    UserName = "alex@mail.com"
                };

                await userManager.CreateAsync(user, "alex1234");
            }
        }

        public static async Task SeedSpecies(AnimalsDbContext context)
        {
            if (!context.Species.Any())
            {
                await context.Species.AddRangeAsync(
                    new SpeciesLookup
                    {
                        Name = "Cat"
                    },
                    new SpeciesLookup
                    {
                        Name = "Dog"
                    }
                );

                await context.SaveChangesAsync();
            }
        }

        public static async Task SeedBreed(AnimalsDbContext context)
        {
            if (!context.Breeds.Any())
            {
                var dogLookup = await context.SpeciesLookup.FirstAsync(s => s.Name == "Dog");
                var catLookup = await context.SpeciesLookup.FirstAsync(s => s.Name == "Cat");

                await context.Breeds.AddRangeAsync(
                    new BreedLookup
                    {
                        Species = dogLookup,
                        Name = "Bloodhound"
                    },
                    new BreedLookup
                    {
                        Species = dogLookup,
                        Name = "Bulldog"
                    },
                    new BreedLookup
                    {
                        Species = dogLookup,
                        Name = "Corgi"
                    },
                    new BreedLookup
                    {
                        Species = catLookup,
                        Name = "Burmese"
                    },
                    new BreedLookup
                    {
                        Species = catLookup,
                        Name = "British Shorthair"
                    },
                    new BreedLookup
                    {
                        Species = catLookup,
                        Name = "Siamese"
                    },
                    new BreedLookup
                    {
                        Species = catLookup,
                        Name = "Maine Coon"
                    },
                    new BreedLookup
                    {
                        Species = catLookup,
                        Name = "Himalayan"
                    }
                );

                await context.SaveChangesAsync();
            }
        }


        public static async Task SeedColors(AnimalsDbContext context)
        {
            if (!context.Colors.Any())
            {
                await context.Colors.AddRangeAsync(
                    new ColorLookup
                    {
                        Name = "Black"
                    },
                    new ColorLookup
                    {
                        Name = "White"
                    },
                    new ColorLookup
                    {
                        Name = "Grey"
                    },
                    new ColorLookup
                    {
                        Name = "Brown"
                    },
                    new ColorLookup
                    {
                        Name = "Ginger"
                    },
                    new ColorLookup
                    {
                        Name = "Multicolor"
                    }
                );

                await context.SaveChangesAsync();
            }
        }

        public static async Task SeedPosts(AnimalsDbContext context)
        {
            if (!context.Posts.Any())
            {
                var user1 = await context.Users.Include(u => u.Posts).FirstAsync(u => u.FirstName == "Nick");
                var user2 = await context.Users.Include(u => u.Posts).FirstAsync(u => u.FirstName == "Alex");
                var dogLookup = await context.SpeciesLookup.Include(s => s.Breeds).FirstAsync(s => s.Name == "Dog");
                var catLookup = await context.SpeciesLookup.Include(s => s.Breeds).FirstAsync(s => s.Name == "Cat");
                var colors = context.Colors.AsEnumerable().ToList();

                context.Posts.AddRange(
                    new Post
                    {
                        Author = user1,
                        Species = dogLookup,
                        Breed = dogLookup.Breeds.First(b => b.Name == "Bulldog"),
                        Color = colors.First(c => c.Name == "White"),
                        Size = Size.Medium,
                        Location = new Location
                        {
                            Address = "University street, 9",
                            Latitude = 47.044221,
                            Longitude = 28.88328,
                        },
                        PostType = PostType.Lost,
                        LostTime = new DateTime(2020, 11, 22, 14, 20, 0),
                        PostTime = new DateTime(2020, 11, 22, 15, 37, 0),
                        Content = "Help me! I've lost my dog!"
                    },
                    new Post
                    {
                        Author = user1,
                        Species = dogLookup,
                        Breed = dogLookup.Breeds.First(b => b.Name == "Corgi"),
                        Color = await context.Colors.FirstAsync(c => c.Name == "Ginger"),
                        Size = Size.Small,
                        PostType = PostType.Lost,
                        Location = new Location
                        {
                            Address = "Balcani 2",
                            Latitude = 47.04355,
                            Longitude = 28.762653
                        },
                        LostTime = new DateTime(2020, 11, 25, 9, 35, 0),
                        PostTime = new DateTime(2020, 11, 25, 10, 10, 0),
                        Content = "Lost my dog :("
                    },
                    new Post
                    {
                        Author = user2,
                        Species = catLookup,
                        Breed = catLookup.Breeds.First(b => b.Name == "Maine Coon"),
                        Color = await context.Colors.FirstAsync(c => c.Name == "Grey"),
                        Size = Size.Big,
                        PostType = PostType.Lost,
                        Location = new Location
                        {
                            Address = "Stefan cel Mare 194",
                            Latitude = 47.034014,
                            Longitude = 28.818413
                        },
                        LostTime = new DateTime(2020, 12, 1, 22, 12, 0),
                        PostTime = new DateTime(2020, 12, 1, 23, 20, 0),
                        Content = "My Precious cat is lost!"
                    },
                    new Post
                    {
                        Author = user2,
                        Species = catLookup,
                        Breed = catLookup.Breeds.First(b => b.Name == "British Shorthair"),
                        Color = await context.Colors.FirstAsync(c => c.Name == "Black"),
                        Size = Size.Medium,
                        PostType = PostType.Lost,
                        Location = new Location
                        {
                            Address = "Strada Socoleni 21",
                            Latitude = 47.060293,
                            Longitude = 28.837375
                        },
                        LostTime = new DateTime(2020, 12, 3, 17, 20, 0),
                        PostTime = new DateTime(2020, 12, 4, 9, 12, 0),
                        Content = "Lost my cat..."
                    }
                );

                await context.SaveChangesAsync();
            }
        }


        public static async Task SeedComments(AnimalsDbContext context)
        {
            if (!context.Comments.Any())
            {
                var user = await context.Users.FirstAsync(u => u.FirstName == "Alex");
                var post = await context.Posts.Include(p => p.Comments).Include(p => p.Author).FirstAsync(p => p.Author.Id != user.Id);

                context.Comments.AddRange(
                    new Comment
                    {
                        Content = "I've seen your dog near the place you've lost it.",
                        Author = user,
                        Post = post,
                        Location = new Location
                        {
                            Address = "",
                            Latitude = 47.044224,
                            Longitude = 28.883282
                        },
                        SeenTime = new DateTime(2020, 12, 4, 9, 12, 0),
                        CommentTime = new DateTime(2020, 12, 4, 14, 0, 0),
                    },
                    new Comment
                    {
                        Content = "My friend also saw your dog somewhere here.",
                        Author = user,
                        Post = post,
                        Location = new Location
                        {
                            Address = "",
                            Latitude = 47.0442245,
                            Longitude = 28.88329
                        },
                        SeenTime = new DateTime(2020, 12, 6, 17, 20, 0),
                        CommentTime = new DateTime(2020, 12, 7, 12, 30, 0),
                    }
                );

                await context.SaveChangesAsync();
            }
        }
    }
}
