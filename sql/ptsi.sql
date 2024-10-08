CREATE TABLE [MonthlyOpeningExpense](
	[OpeningId] [float] NOT NULL,
	[ProjectCoordinator] [float] NULL,
	[AdditionalStaff] [bit]  DEFAULT ((0)) NULL,
	[IsWorked] [bit]  DEFAULT ((0)) NULL
	[StaffId] [float] NULL,
	[Year] [int] NOT NULL,
	[Month] [int] NOT NULL,
	[OpeningDate] [date] NULL,
	[ClosingDate] [date] NULL,
	[OpeningBalance] [float] NULL,
	[ClosingBalance] [float] NULL,
	[CreatedBy] [int] NULL,
	[CreatedDate] [varchar](255) NULL,
	[UpdatedBy] [int] NULL,
	[UpdatedDate] [varchar](255) NULL
) ON [PRIMARY]
GO

CREATE SEQUENCE dbo.MonthlyOpeningExpense_SEQ
START WITH 1
INCREMENT BY 1
MINVALUE 1
MAXVALUE 1000000
CYCLE;

ALTER TABLE Project ADD ProjectCoordinator FLOAT NULL;
ALTER TABLE ZoneMaster ADD ProjectCoordinator VARCHAR(255) NULL;
ALTER TABLE Staff ADD ProjectCoordinator FLOAT NULL;

ALTER TABLE Users ADD UserType VARCHAR(30) NOT NULL DEFAULT 'USER';
UPDATE Users SET UserType = 'SUPER_ADMIN' WHERE UserName = 'admin';






CREATE TABLE [dbo].[MonthlyExpense](
	[ExpenseId] [float] NOT NULL,
	[Year] [float] NOT NULL,
	[Month] [float] NOT NULL,
	[AdditionalStaff] [BIT] DEFAULT 0,
	[ProjectCoordinator] [float] NULL,
	[StaffId] [float] NULL,
	[Tea] [float] NULL,
	[Telephone] [float] NULL,
	[Petrol] [float] NULL,
	[CreatedBy] [int] NULL,
	[CreatedDate] [varchar](255) NULL,
	[UpdatedBy] [int] NULL,
	[UpdatedDate] [varchar](255) NULL
) ON [PRIMARY]
GO


CREATE SEQUENCE dbo.MonthlyExpense_SEQ
START WITH 1
INCREMENT BY 1
MINVALUE 1
MAXVALUE 1000000
CYCLE;
