using Microsoft.EntityFrameworkCore.Migrations;

namespace LostAnimalsAPI.Migrations
{
    public partial class temporarilyRemovedLostTime : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Breed",
                table: "Posts");

            migrationBuilder.DropColumn(
                name: "Color",
                table: "Posts");

            migrationBuilder.DropColumn(
                name: "LostTime",
                table: "Posts");

            migrationBuilder.DropColumn(
                name: "Species",
                table: "Posts");

            migrationBuilder.AddColumn<long>(
                name: "BreedId",
                table: "Posts",
                nullable: true);

            migrationBuilder.AddColumn<long>(
                name: "ColorId",
                table: "Posts",
                nullable: true);

            migrationBuilder.AddColumn<long>(
                name: "SpeciesId",
                table: "Posts",
                nullable: true);

            migrationBuilder.CreateTable(
                name: "BreedLookup",
                columns: table => new
                {
                    Id = table.Column<long>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_BreedLookup", x => x.Id);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Posts_BreedId",
                table: "Posts",
                column: "BreedId");

            migrationBuilder.CreateIndex(
                name: "IX_Posts_ColorId",
                table: "Posts",
                column: "ColorId");

            migrationBuilder.CreateIndex(
                name: "IX_Posts_SpeciesId",
                table: "Posts",
                column: "SpeciesId");

            migrationBuilder.AddForeignKey(
                name: "FK_Posts_BreedLookup_BreedId",
                table: "Posts",
                column: "BreedId",
                principalTable: "BreedLookup",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Posts_Colors_ColorId",
                table: "Posts",
                column: "ColorId",
                principalTable: "Colors",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Posts_SpeciesLookup_SpeciesId",
                table: "Posts",
                column: "SpeciesId",
                principalTable: "SpeciesLookup",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Posts_BreedLookup_BreedId",
                table: "Posts");

            migrationBuilder.DropForeignKey(
                name: "FK_Posts_Colors_ColorId",
                table: "Posts");

            migrationBuilder.DropForeignKey(
                name: "FK_Posts_SpeciesLookup_SpeciesId",
                table: "Posts");

            migrationBuilder.DropTable(
                name: "BreedLookup");

            migrationBuilder.DropIndex(
                name: "IX_Posts_BreedId",
                table: "Posts");

            migrationBuilder.DropIndex(
                name: "IX_Posts_ColorId",
                table: "Posts");

            migrationBuilder.DropIndex(
                name: "IX_Posts_SpeciesId",
                table: "Posts");

            migrationBuilder.DropColumn(
                name: "BreedId",
                table: "Posts");

            migrationBuilder.DropColumn(
                name: "ColorId",
                table: "Posts");

            migrationBuilder.DropColumn(
                name: "SpeciesId",
                table: "Posts");

            migrationBuilder.AddColumn<string>(
                name: "Breed",
                table: "Posts",
                type: "nvarchar(max)",
                nullable: true);

            migrationBuilder.AddColumn<string>(
                name: "Color",
                table: "Posts",
                type: "nvarchar(max)",
                nullable: true);

            migrationBuilder.AddColumn<int>(
                name: "LostTime",
                table: "Posts",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.AddColumn<string>(
                name: "Species",
                table: "Posts",
                type: "nvarchar(max)",
                nullable: true);
        }
    }
}
