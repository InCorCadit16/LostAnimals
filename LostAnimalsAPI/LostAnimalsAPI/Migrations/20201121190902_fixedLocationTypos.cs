using Microsoft.EntityFrameworkCore.Migrations;

namespace LostAnimalsAPI.Migrations
{
    public partial class fixedLocationTypos : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Longtitude",
                table: "Locations");

            migrationBuilder.AddColumn<double>(
                name: "Longitude",
                table: "Locations",
                nullable: false,
                defaultValue: 0.0);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Longitude",
                table: "Locations");

            migrationBuilder.AddColumn<double>(
                name: "Longtitude",
                table: "Locations",
                type: "float",
                nullable: false,
                defaultValue: 0.0);
        }
    }
}
