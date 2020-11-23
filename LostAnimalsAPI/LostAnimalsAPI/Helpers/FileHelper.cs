using LostAnimalsAPI.Helpers.Base;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Helpers
{
    public class FileHelper : IFileHelper
    {
        private class FilePathConsts
        {
            public const string PostImagesPath = "assets/posts/";
            public const string UserImagesPath = "assets/users/";
            public const string SmallPostfix = "_small";
            public const string Format = ".jpg";

            public const string UserDefaultImage = "assets/default/user.jpg";
            public const string PostDefaultImage = "assets/default/post.jpg";
        }

        public async Task<byte[]> LoadFileAsync(long id, bool forPost = true, bool fullSize = true)
        {
            var path = new StringBuilder(forPost ? FilePathConsts.PostImagesPath : FilePathConsts.UserImagesPath);
            path.Append(id);

            if (!fullSize)
            {
                path.Append(FilePathConsts.SmallPostfix);
            }

            if (File.Exists(path.ToString()))
            {
                return await File.ReadAllBytesAsync(path.ToString());
            } else
            {
                string defaultPath = forPost ? FilePathConsts.PostDefaultImage : FilePathConsts.UserDefaultImage;
                return await File.ReadAllBytesAsync(defaultPath);
            }
        }
    }
}
