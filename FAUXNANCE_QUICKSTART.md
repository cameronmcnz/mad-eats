# Quick Start: Fauxnance API Integration

## 5-Minute Setup

### Step 1: Configure Environment
```bash
# In .env file, add:
FAUXNANCE_API_KEY=your-api-key-from-instructor
```

### Step 2: Build & Run
```bash
cd api
mvn clean install
mvn spring-boot:run
```

### Step 3: Test the Integration
```bash
# Get a quote
curl http://localhost:8099/api/v1/market/quotes/AAPL

# Check API health
curl http://localhost:8099/api/v1/market/health
```

## Key Features

✅ **Real-time Quotes** - Latest prices with bid/ask spreads  
✅ **Historical Candles** - OHLCV data for technical analysis  
✅ **Smart Caching** - Built-in 5-minute cache for performance  
✅ **Order Validation** - Validates orders against market prices  
✅ **Error Handling** - Comprehensive logging and error management  

## Common Tasks

### 1. Get Current Stock Price
```java
@Autowired
private QuoteService quoteService;

double price = quoteService.getCurrentPrice("AAPL");
```

### 2. Analyze Price Changes
```java
Map<String, Double> metrics = quoteService.getChangeMetrics("AAPL");
// Contains: change, changePercent, previousClose, currentPrice
```

### 3. Get Bid-Ask Spread
```java
Map<String, Double> spread = quoteService.getSpread("AAPL");
// Contains: bid, ask, spread, spreadBps
```

### 4. Fetch Historical Data
```java
CandlesResponse response = fauxnanceClient.getCandlesLastDays("AAPL", 30);
List<Candle> candles = response.getData().getCandles();
```

### 5. Clear Cache
```java
quoteService.clearCache();  // Clear all
quoteService.clearCacheForSymbol("AAPL");  // Clear specific symbol
```

## Supported Symbols

| Type | Examples |
|------|----------|
| US Stocks | AAPL, MSFT, GOOGL, TSLA |
| India Stocks | INFY.NS, TCS.NS, WIPRO.NS |
| Forex | FX:EURUSD, FX:GBPUSD, FX:JPYUSD |
| Crypto | X:BTC-USD, X:ETH-USD |

## Architecture

```
Request → MarketDataController 
         ↓
      QuoteService (with caching)
         ↓
    FauxnanceClient
         ↓
  Fauxnance API
```

## Files Added

| File | Purpose |
|------|---------|
| `FauxnanceClient.java` | Low-level API client |
| `QuoteService.java` | High-level service with caching |
| `MarketDataController.java` | REST API endpoints |
| `Quote.java`, `Candle.java` | Data Transfer Objects |
| `WebClientConfig.java` | Spring configuration |

## Configuration

All properties configurable in `application.properties`:
```properties
fauxnance.api.url=https://y4t9nq2bqf.execute-api.eu-west-2.amazonaws.com/v1
fauxnance.api.key=${FAUXNANCE_API_KEY}
fauxnance.api.timeout=10000  # milliseconds
fauxnance.api.cache.ttl=300  # seconds
```

## API Endpoints

- `GET /api/v1/market/quotes/{symbol}` - Get quote
- `GET /api/v1/market/spread/{symbol}` - Get bid-ask spread
- `GET /api/v1/market/changes/{symbol}` - Get change metrics
- `GET /api/v1/market/price/{symbol}` - Get current price
- `GET /api/v1/market/candles/{symbol}` - Get historical OHLCV
- `GET /api/v1/market/health` - Check API health
- `POST /api/v1/market/cache/clear` - Clear cache

See [FAUXNANCE_INTEGRATION.md](FAUXNANCE_INTEGRATION.md) for detailed documentation.

## Troubleshooting

| Problem | Solution |
|---------|----------|
| API key errors | Check `FAUXNANCE_API_KEY` in `.env` |
| Symbol not found | Use correct format (AAPL, INFY.NS, FX:EURUSD, X:BTC-USD) |
| Timeout errors | Check network connectivity and API status |
| Rate limited | Wait until next UTC day or increase quota |

## Need Help?

See [FAUXNANCE_INTEGRATION.md](FAUXNANCE_INTEGRATION.md) for:
- Full API documentation
- Detailed usage examples
- Advanced configuration options
- Performance tuning guides
- Error handling strategies
