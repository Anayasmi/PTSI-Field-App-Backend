USE [PTSI_APPLICATION]
GO

/****** Object:  Table [dbo].[MonthlyOpeningExpense]    Script Date: 8/30/2024 6:35:28 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[MonthlyOpeningExpense](
	[ExpenseId] [float] NOT NULL,
	[Month] [int] NULL,
	[Year] [int] NULL,
	[StaffId] [float] NULL,
	[OpeningExpense] [float] NULL,
	[CreatedBy] [int] NULL,
	[CreatedDate] [varchar](255) NULL,
	[UpdatedBy] [int] NULL,
	[UpdatedDate] [varchar](255) NULL,
 CONSTRAINT [pk_monthlyopeningexpense] PRIMARY KEY CLUSTERED
(
	[ExpenseId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO


CREATE SEQUENCE dbo.MonthlyOpeningExpense_SEQ
START WITH 1
INCREMENT BY 1
MINVALUE 1
MAXVALUE 1000000
CYCLE;

ALTER TABLE [dbo].[Project] ADD [ProjectCoordinator] [int] NULL;
ALTER TABLE [dbo].[Staff] ADD [ProjectCoordinator] [int] NULL;
ALTER TABLE [dbo].[Staff] ADD [Tea] [int] NULL;
ALTER TABLE [dbo].[Staff] ADD [Telephone] [int] NULL;
ALTER TABLE [dbo].[Staff] ADD [Petrol] [int] NULL;
