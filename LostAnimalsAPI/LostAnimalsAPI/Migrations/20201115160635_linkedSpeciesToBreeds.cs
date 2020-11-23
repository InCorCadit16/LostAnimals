using Microsoft.EntityFrameworkCore.Migrations;

namespace LostAnimalsAPI.Migrations
{
    public partial class linkedSpeciesToBreeds : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<long>(
                name: "SpeciesId",
                table: "BreedLookup",
                nullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_BreedLookup_SpeciesId",
                table: "BreedLookup",
                column: "SpeciesId");

            migrationBuilder.AddForeignKey(
                name: "FK_BreedLookup_SpeciesLookup_SpeciesId",
                table: "BreedLookup",
                column: "SpeciesId",
                principalTable: "SpeciesLookup",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_BreedLookup_SpeciesLookup_SpeciesId",
                table: "BreedLookup");

            migrationBuilder.DropIndex(
                name: "IX_BreedLookup_SpeciesId",
                table: "BreedLookup");

            migrationBuilder.DropColumn(
                name: "SpeciesId",
                table: "BreedLookup");
        }
    }
}
