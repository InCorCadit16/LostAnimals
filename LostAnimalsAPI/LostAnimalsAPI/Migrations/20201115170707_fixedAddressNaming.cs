using Microsoft.EntityFrameworkCore.Migrations;

namespace LostAnimalsAPI.Migrations
{
    public partial class fixedAddressNaming : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_BreedLookup_SpeciesLookup_SpeciesId",
                table: "BreedLookup");

            migrationBuilder.DropForeignKey(
                name: "FK_Posts_Locaitons_AdressId",
                table: "Posts");

            migrationBuilder.DropForeignKey(
                name: "FK_Posts_BreedLookup_BreedId",
                table: "Posts");

            migrationBuilder.DropUniqueConstraint(
                name: "AK_SpeciesLookup_Name",
                table: "SpeciesLookup");

            migrationBuilder.DropIndex(
                name: "IX_Posts_AdressId",
                table: "Posts");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Locaitons",
                table: "Locaitons");

            migrationBuilder.DropPrimaryKey(
                name: "PK_BreedLookup",
                table: "BreedLookup");

            migrationBuilder.DropColumn(
                name: "AdressId",
                table: "Posts");

            migrationBuilder.RenameTable(
                name: "Locaitons",
                newName: "Locations");

            migrationBuilder.RenameTable(
                name: "BreedLookup",
                newName: "Breeds");

            migrationBuilder.RenameIndex(
                name: "IX_BreedLookup_SpeciesId",
                table: "Breeds",
                newName: "IX_Breeds_SpeciesId");

            migrationBuilder.AlterColumn<string>(
                name: "Name",
                table: "SpeciesLookup",
                nullable: true,
                oldClrType: typeof(string),
                oldType: "nvarchar(450)");

            migrationBuilder.AlterColumn<string>(
                name: "Name",
                table: "Colors",
                nullable: true,
                oldClrType: typeof(string),
                oldType: "nvarchar(max)",
                oldNullable: true);

            migrationBuilder.AddColumn<long>(
                name: "PostId",
                table: "Locations",
                nullable: false,
                defaultValue: 0L);

            migrationBuilder.AddPrimaryKey(
                name: "PK_Locations",
                table: "Locations",
                column: "Id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Breeds",
                table: "Breeds",
                column: "Id");

            migrationBuilder.CreateIndex(
                name: "IX_SpeciesLookup_Name",
                table: "SpeciesLookup",
                column: "Name");

            migrationBuilder.CreateIndex(
                name: "IX_Colors_Name",
                table: "Colors",
                column: "Name");

            migrationBuilder.CreateIndex(
                name: "IX_Locations_PostId",
                table: "Locations",
                column: "PostId",
                unique: true);

            migrationBuilder.AddForeignKey(
                name: "FK_Breeds_SpeciesLookup_SpeciesId",
                table: "Breeds",
                column: "SpeciesId",
                principalTable: "SpeciesLookup",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Locations_Posts_PostId",
                table: "Locations",
                column: "PostId",
                principalTable: "Posts",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_Posts_Breeds_BreedId",
                table: "Posts",
                column: "BreedId",
                principalTable: "Breeds",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Breeds_SpeciesLookup_SpeciesId",
                table: "Breeds");

            migrationBuilder.DropForeignKey(
                name: "FK_Locations_Posts_PostId",
                table: "Locations");

            migrationBuilder.DropForeignKey(
                name: "FK_Posts_Breeds_BreedId",
                table: "Posts");

            migrationBuilder.DropIndex(
                name: "IX_SpeciesLookup_Name",
                table: "SpeciesLookup");

            migrationBuilder.DropIndex(
                name: "IX_Colors_Name",
                table: "Colors");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Locations",
                table: "Locations");

            migrationBuilder.DropIndex(
                name: "IX_Locations_PostId",
                table: "Locations");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Breeds",
                table: "Breeds");

            migrationBuilder.DropColumn(
                name: "PostId",
                table: "Locations");

            migrationBuilder.RenameTable(
                name: "Locations",
                newName: "Locaitons");

            migrationBuilder.RenameTable(
                name: "Breeds",
                newName: "BreedLookup");

            migrationBuilder.RenameIndex(
                name: "IX_Breeds_SpeciesId",
                table: "BreedLookup",
                newName: "IX_BreedLookup_SpeciesId");

            migrationBuilder.AlterColumn<string>(
                name: "Name",
                table: "SpeciesLookup",
                type: "nvarchar(450)",
                nullable: false,
                oldClrType: typeof(string),
                oldNullable: true);

            migrationBuilder.AddColumn<long>(
                name: "AdressId",
                table: "Posts",
                type: "bigint",
                nullable: true);

            migrationBuilder.AlterColumn<string>(
                name: "Name",
                table: "Colors",
                type: "nvarchar(max)",
                nullable: true,
                oldClrType: typeof(string),
                oldNullable: true);

            migrationBuilder.AddUniqueConstraint(
                name: "AK_SpeciesLookup_Name",
                table: "SpeciesLookup",
                column: "Name");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Locaitons",
                table: "Locaitons",
                column: "Id");

            migrationBuilder.AddPrimaryKey(
                name: "PK_BreedLookup",
                table: "BreedLookup",
                column: "Id");

            migrationBuilder.CreateIndex(
                name: "IX_Posts_AdressId",
                table: "Posts",
                column: "AdressId");

            migrationBuilder.AddForeignKey(
                name: "FK_BreedLookup_SpeciesLookup_SpeciesId",
                table: "BreedLookup",
                column: "SpeciesId",
                principalTable: "SpeciesLookup",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Posts_Locaitons_AdressId",
                table: "Posts",
                column: "AdressId",
                principalTable: "Locaitons",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Posts_BreedLookup_BreedId",
                table: "Posts",
                column: "BreedId",
                principalTable: "BreedLookup",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }
    }
}
