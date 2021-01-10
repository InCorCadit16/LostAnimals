using LostAnimalsAPI.Helpers.Base;
using System.Drawing;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using static LostAnimalsAPI.Helpers.Base.IFileHelper;

namespace LostAnimalsAPI.Helpers
{
    public class FileHelper : IFileHelper
    {
        private class FilePathConsts
        {
            public const string PostImagesPath = "assets/images/posts/";
            public const string UserImagesPath = "assets/images/users/";
            public const string ShelterImagesPath = "assets/images/shelters/";
            public const string SmallPostfix = "_small";
            public const string Format = ".jpg";

            public const string UserDefaultImage = "assets/images/default/user.jpg";
        }

       

        public async Task<byte[]> LoadFileAsync(long id, ObjectType type, bool fullSize = true)
        {
            var path = new StringBuilder(
                type == ObjectType.Post ? 
                FilePathConsts.PostImagesPath : 
                (type == ObjectType.User ? FilePathConsts.UserImagesPath : FilePathConsts.ShelterImagesPath));
            path.Append(id);

            if (!fullSize)
            {
                path.Append(FilePathConsts.SmallPostfix);
            }

            path.Append(FilePathConsts.Format);

            if (File.Exists(path.ToString()))
            {
                return await File.ReadAllBytesAsync(path.ToString());
            }
            else
            {
                return type == ObjectType.User ? await File.ReadAllBytesAsync(FilePathConsts.UserDefaultImage) : null;
            }
        }

        public void SaveFile(long id, byte[] source, ObjectType type)
        {
            var path = new StringBuilder(type == ObjectType.Post ?
                FilePathConsts.PostImagesPath :
                (type == ObjectType.User ? FilePathConsts.UserImagesPath : FilePathConsts.ShelterImagesPath));
            path.Append(id);

            var bitmap = new Bitmap(Bitmap.FromStream(new MemoryStream(source)));

            Bitmap smallImage = Resize(bitmap, 300);
            smallImage.Save(path.ToString() + FilePathConsts.Format);

            Bitmap bigImage = Resize(bitmap, 800);
            path.Append(FilePathConsts.SmallPostfix);
            bigImage.Save(path.ToString() + FilePathConsts.Format);
        }

        private Bitmap Resize(Bitmap image, int height)
        {
            int width = image.Width * height / image.Height;

            return new Bitmap(image, width, height);
        }
    }
}
