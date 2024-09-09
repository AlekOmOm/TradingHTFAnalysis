import csv
import json
import os
import time

# Paths
trading_vault_path = os.getenv('TRADING_VAULT_PATH')
github_repository_path = os.getenv('GITHUB_LOCAL_REPO_PATH')
output_chart_data_path = os.getenv('CHART_DATA_JSON_PATH')


csv_input_path = (r'' + trading_vault_path + r'\png\Downloads\CME_MINI_NQ1!, 5_a4717.csv')
output_path = (r'' + output_chart_data_path)


def timestamp_to_datetime(timestamp):
    """Convert UNIX timestamp to a human-readable datetime format."""
    return time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(int(timestamp)))


def process_csv_to_json(input_file, output_file):
    # Create an empty list to hold JSON data
    json_data = []

    # Open the input CSV file
    with open(input_file, 'r') as infile:
        reader = csv.reader(infile)
        headers = next(reader)  # Skip the first row of headers

        # Iterate through the rows in the CSV file
        for row in reader:
            timestamp = row[0]
            open_price = row[1]
            high_price = row[2]
            low_price = row[3]
            close_price = row[4]

            # Convert the timestamp to a human-readable datetime
            datetime = timestamp_to_datetime(timestamp)

            # Create a dictionary for each candle (row in CSV)
            candle_data = {
                "datetime": datetime,
                "open": float(open_price),
                "high": float(high_price),
                "low": float(low_price),
                "close": float(close_price)
            }

            # Append the candle data to the JSON data list
            json_data.append(candle_data)

    # Write the JSON data to a file
    with open(output_file, 'w') as outfile:
        json.dump(json_data, outfile, indent=4)

    print(f"CSV processed and saved as JSON at: {output_file}")


# Ensure output directory exists
os.makedirs(os.path.dirname(output_path), exist_ok=True)

# Process the CSV and save as JSON
process_csv_to_json(csv_input_path, output_path)
